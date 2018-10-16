import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArtikel } from 'app/shared/model/artikel.model';

@Component({
    selector: 'jhi-artikel-detail',
    templateUrl: './artikel-detail.component.html'
})
export class ArtikelDetailComponent implements OnInit {
    artikel: IArtikel;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ artikel }) => {
            this.artikel = artikel;
        });
    }

    previousState() {
        window.history.back();
    }
}
