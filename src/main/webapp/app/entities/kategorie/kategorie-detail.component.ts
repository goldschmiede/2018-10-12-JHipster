import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKategorie } from 'app/shared/model/kategorie.model';

@Component({
    selector: 'jhi-kategorie-detail',
    templateUrl: './kategorie-detail.component.html'
})
export class KategorieDetailComponent implements OnInit {
    kategorie: IKategorie;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ kategorie }) => {
            this.kategorie = kategorie;
        });
    }

    previousState() {
        window.history.back();
    }
}
