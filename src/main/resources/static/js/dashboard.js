(function () {
    'use strict';

    const API = '/api';
    const $ = (id) => document.getElementById(id);
    const fullName = (p) => (p ? `${p.prenom} ${p.nom}` : '—');
    const esc = (v) => String(v ?? '—').replace(/[&<>"']/g, (c) =>
        ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));

    // ---------- Description des 6 entités ----------
    const ENT = {
        etudiants: {
            title: 'Étudiants', singular: 'étudiant', subtitle: 'Gestion des étudiants',
            cols: [['Nom', (e) => e.nom], ['Prénom', (e) => e.prenom], ['Email', (e) => e.email], ['Classe', (e) => e.classe?.nom]],
            fields: [
                { n: 'nom', l: 'Nom', t: 'text', req: true },
                { n: 'prenom', l: 'Prénom', t: 'text', req: true },
                { n: 'email', l: 'Email', t: 'email', req: true },
                { n: 'classe', l: 'Classe', t: 'ref', ref: 'classes', label: (c) => c.nom }
            ]
        },
        enseignants: {
            title: 'Enseignants', singular: 'enseignant', subtitle: 'Gestion des enseignants',
            cols: [['Nom', (e) => e.nom], ['Prénom', (e) => e.prenom], ['Email', (e) => e.email]],
            fields: [
                { n: 'nom', l: 'Nom', t: 'text', req: true },
                { n: 'prenom', l: 'Prénom', t: 'text', req: true },
                { n: 'email', l: 'Email', t: 'email', req: true }
            ]
        },
        classes: {
            title: 'Classes', singular: 'classe', subtitle: 'Gestion des classes',
            cols: [['Nom', (c) => c.nom], ['Niveau', (c) => c.niveau]],
            fields: [
                { n: 'nom', l: 'Nom', t: 'text', req: true },
                { n: 'niveau', l: 'Niveau', t: 'number', min: 0, step: 1 }
            ]
        },
        cours: {
            title: 'Cours', singular: 'cours', subtitle: 'Gestion des cours',
            cols: [['Titre', (c) => c.titre], ['Volume horaire', (c) => (c.volumeHoraire != null ? c.volumeHoraire + ' h' : null)],
                ['Enseignant', (c) => (c.enseignant ? fullName(c.enseignant) : null)], ['Classe', (c) => c.classe?.nom]],
            fields: [
                { n: 'titre', l: 'Titre', t: 'text', req: true },
                { n: 'volumeHoraire', l: 'Volume horaire (h)', t: 'number', min: 0, step: 1 },
                { n: 'enseignant', l: 'Enseignant', t: 'ref', ref: 'enseignants', label: fullName },
                { n: 'classe', l: 'Classe', t: 'ref', ref: 'classes', label: (c) => c.nom }
            ]
        },
        inscriptions: {
            title: 'Inscriptions', singular: 'inscription', subtitle: 'Inscriptions des étudiants aux cours',
            cols: [['Date', (i) => i.dateInscription], ['Étudiant', (i) => fullName(i.etudiant)], ['Cours', (i) => i.cours?.titre]],
            fields: [
                { n: 'dateInscription', l: 'Date', t: 'date' },
                { n: 'etudiant', l: 'Étudiant', t: 'ref', ref: 'etudiants', label: fullName, req: true },
                { n: 'cours', l: 'Cours', t: 'ref', ref: 'cours', label: (c) => c.titre, req: true }
            ]
        },
        notes: {
            title: 'Notes', singular: 'note', subtitle: 'Notes obtenues (sur 20)',
            cols: [['Étudiant', (n) => fullName(n.inscription?.etudiant)], ['Cours', (n) => n.inscription?.cours?.titre],
                ['Note', (n) => (n.valeur != null ? n.valeur + ' / 20' : null)], ['Commentaire', (n) => n.commentaire], ['Date', (n) => n.dateEvaluation]],
            fields: [
                { n: 'inscription', l: 'Inscription', t: 'ref', ref: 'inscriptions', req: true,
                    label: (i) => `#${i.id} — ${fullName(i.etudiant)} / ${i.cours?.titre ?? '?'}` },
                { n: 'valeur', l: 'Note (0 à 20)', t: 'number', min: 0, max: 20, step: 0.25, req: true },
                { n: 'commentaire', l: 'Commentaire', t: 'text' },
                { n: 'dateEvaluation', l: "Date d'évaluation", t: 'date' }
            ]
        }
    };
    const KEYS = Object.keys(ENT);
    const data = {};
    let current = 'dashboard';
    let editingId = null;

    // ---------- Appels API ----------
    async function request(path, options) {
        const res = await fetch(API + path, options);
        if (!res.ok) {
            let msg = res.status + ' ' + res.statusText;
            try { const body = await res.json(); if (body.message) msg = body.message; } catch (e) { /* ignore */ }
            throw new Error(msg);
        }
        return res.status === 204 ? null : res.json();
    }

    async function loadAll() {
        try {
            const results = await Promise.all(KEYS.map((k) => request('/' + k)));
            KEYS.forEach((k, i) => { data[k] = results[i]; });
            $('banner').classList.add('hidden');
        } catch (e) {
            KEYS.forEach((k) => { data[k] = data[k] || []; });
            $('banner').innerHTML = "<strong>Impossible de charger les données.</strong><br>Vérifiez que MySQL est démarré, que la base <code>school_db</code> existe et que l'identifiant/mot de passe dans <code>application.properties</code> sont corrects.<br><small>" + esc(e.message) + '</small>';
            $('banner').classList.remove('hidden');
        }
        render();
    }

    // ---------- Rendu ----------
    function toast(msg, isError) {
        const t = $('toast');
        t.textContent = msg;
        t.className = 'toast' + (isError ? ' error' : '');
        clearTimeout(toast.timer);
        toast.timer = setTimeout(() => t.classList.add('hidden'), 3500);
    }

    function miniTable(headers, rows) {
        if (!rows.length) return '<p class="empty">Aucune donnée.</p>';
        return '<table><thead><tr>' + headers.map((h) => `<th>${esc(h)}</th>`).join('') + '</tr></thead><tbody>' +
            rows.map((r) => '<tr>' + r.map((c) => `<td>${esc(c)}</td>`).join('') + '</tr>').join('') + '</tbody></table>';
    }

    function renderDashboard() {
        const labels = { etudiants: 'Étudiants', enseignants: 'Enseignants', classes: 'Classes', cours: 'Cours', inscriptions: 'Inscriptions', notes: 'Notes' };
        const notes = (data.notes || []).filter((n) => n.valeur != null);
        const avg = notes.length ? (notes.reduce((s, n) => s + n.valeur, 0) / notes.length).toFixed(2) : '—';

        let html = KEYS.map((k) => `<div class="card" data-go="${k}"><h3>${labels[k]}</h3><p>${(data[k] || []).length}</p></div>`).join('');
        html += `<div class="card" data-go="notes"><h3>Moyenne générale</h3><p>${avg}${avg === '—' ? '' : ' / 20'}</p></div>`;
        $('stat-cards').innerHTML = html;

        // Étudiants par classe
        const counts = {};
        (data.classes || []).forEach((c) => { counts[c.id] = { nom: c.nom, n: 0 }; });
        let sans = 0;
        (data.etudiants || []).forEach((e) => { if (e.classe && counts[e.classe.id]) counts[e.classe.id].n++; else sans++; });
        const rows = Object.values(counts);
        if (sans) rows.push({ nom: 'Sans classe', n: sans });
        const max = Math.max(1, ...rows.map((r) => r.n));
        $('chart-classes').innerHTML = rows.length
            ? rows.map((r) => `<div class="bar-row"><span class="bar-label" title="${esc(r.nom)}">${esc(r.nom)}</span><div class="bar-track"><div class="bar-fill" style="width:${(r.n / max) * 100}%"></div></div><span class="bar-value">${r.n}</span></div>`).join('')
            : '<p class="empty">Aucune classe.</p>';

        const last = (arr) => (arr || []).slice().sort((a, b) => b.id - a.id).slice(0, 5);
        $('recent-inscriptions').innerHTML = miniTable(['Date', 'Étudiant', 'Cours'],
            last(data.inscriptions).map((i) => [i.dateInscription, fullName(i.etudiant), i.cours?.titre]));
        $('recent-etudiants').innerHTML = miniTable(['Nom', 'Email', 'Classe'],
            last(data.etudiants).map((e) => [fullName(e), e.email, e.classe?.nom]));
        $('recent-notes').innerHTML = miniTable(['Étudiant', 'Cours', 'Note'],
            last(data.notes).map((n) => [fullName(n.inscription?.etudiant), n.inscription?.cours?.titre, n.valeur != null ? n.valeur + ' / 20' : null]));
    }

    function renderEntity() {
        const cfg = ENT[current];
        const q = $('search').value.trim().toLowerCase();
        const items = (data[current] || []).filter((it) =>
            !q || cfg.cols.some((c) => String(c[1](it) ?? '').toLowerCase().includes(q)));

        let html = '<thead><tr><th>#</th>' + cfg.cols.map((c) => `<th>${esc(c[0])}</th>`).join('') + '<th></th></tr></thead><tbody>';
        if (!items.length) {
            html += `<tr><td colspan="${cfg.cols.length + 2}" class="empty">Aucun élément.</td></tr>`;
        } else {
            html += items.map((it) => `<tr><td>${it.id}</td>` + cfg.cols.map((c) => `<td>${esc(c[1](it))}</td>`).join('') +
                `<td class="actions"><button class="btn btn-sm" data-edit="${it.id}">Modifier</button><button class="btn btn-sm btn-danger" data-del="${it.id}">Supprimer</button></td></tr>`).join('');
        }
        $('entity-table').innerHTML = html + '</tbody>';
    }

    function render() {
        if (current === 'dashboard') renderDashboard(); else renderEntity();
    }

    // ---------- Navigation ----------
    function navigate() {
        const view = location.hash.replace('#', '');
        current = view === 'dashboard' || ENT[view] ? view : 'dashboard';
        document.querySelectorAll('#nav a').forEach((a) => a.classList.toggle('active', a.dataset.view === current));
        const isDash = current === 'dashboard';
        $('view-dashboard').classList.toggle('hidden', !isDash);
        $('view-entity').classList.toggle('hidden', isDash);
        $('page-title').textContent = isDash ? 'Dashboard' : ENT[current].title;
        $('page-subtitle').textContent = isDash ? "Vue d'ensemble de la gestion scolaire" : ENT[current].subtitle;
        $('search').value = '';
        render();
    }

    // ---------- Formulaire (ajout / modification) ----------
    function openForm(id) {
        const cfg = ENT[current];
        editingId = id ?? null;
        const obj = id != null ? data[current].find((x) => x.id === id) : {};
        $('modal-title').textContent = (id != null ? 'Modifier ' : 'Ajouter ') + (cfg.singular.match(/^[aeiouéè]/i) ? "l'" : 'le ') + cfg.singular;

        $('modal-fields').innerHTML = cfg.fields.map((f) => {
            const star = f.req ? ' *' : '';
            if (f.t === 'ref') {
                const sel = obj[f.n]?.id;
                const opts = (data[f.ref] || []).map((r) => `<option value="${r.id}" ${r.id === sel ? 'selected' : ''}>${esc(f.label(r))}</option>`).join('');
                return `<div class="field"><label>${esc(f.l)}${star}</label><select name="${f.n}" ${f.req ? 'required' : ''}><option value="">${f.req ? '— Choisir —' : '— Aucun —'}</option>${opts}</select></div>`;
            }
            const val = obj[f.n] ?? (f.t === 'date' && id == null ? new Date().toISOString().slice(0, 10) : '');
            const attrs = [f.req ? 'required' : '', f.min != null ? `min="${f.min}"` : '', f.max != null ? `max="${f.max}"` : '', f.step != null ? `step="${f.step}"` : ''].join(' ');
            return `<div class="field"><label>${esc(f.l)}${star}</label><input type="${f.t}" name="${f.n}" value="${esc(val === '—' ? '' : val)}" ${attrs}></div>`;
        }).join('');
        $('modal').showModal();
    }

    async function submitForm(ev) {
        ev.preventDefault();
        const cfg = ENT[current];
        const fd = new FormData($('modal-form'));
        const body = {};
        cfg.fields.forEach((f) => {
            const raw = (fd.get(f.n) ?? '').toString().trim();
            if (f.t === 'ref') body[f.n] = raw ? { id: Number(raw) } : null;
            else if (f.t === 'number') body[f.n] = raw === '' ? null : Number(raw);
            else body[f.n] = raw === '' ? (f.req ? '' : null) : raw;
        });
        try {
            if (editingId != null) await request(`/${current}/${editingId}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
            else await request('/' + current, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
            $('modal').close();
            toast('Enregistré avec succès');
            await loadAll();
        } catch (e) {
            toast(e.message, true);
        }
    }

    async function remove(id) {
        if (!confirm('Supprimer cet élément ?')) return;
        try {
            await request(`/${current}/${id}`, { method: 'DELETE' });
            toast('Supprimé');
            await loadAll();
        } catch (e) {
            toast(e.message, true);
        }
    }

    // ---------- Événements ----------
    window.addEventListener('hashchange', navigate);
    $('search').addEventListener('input', renderEntity);
    $('btn-add').addEventListener('click', () => openForm(null));
    $('modal-cancel').addEventListener('click', () => $('modal').close());
    $('modal-form').addEventListener('submit', submitForm);
    $('stat-cards').addEventListener('click', (ev) => {
        const card = ev.target.closest('[data-go]');
        if (card) location.hash = card.dataset.go;
    });
    $('entity-table').addEventListener('click', (ev) => {
        const b = ev.target.closest('button');
        if (!b) return;
        if (b.dataset.edit) openForm(Number(b.dataset.edit));
        if (b.dataset.del) remove(Number(b.dataset.del));
    });

    navigate();
    loadAll();
})();
