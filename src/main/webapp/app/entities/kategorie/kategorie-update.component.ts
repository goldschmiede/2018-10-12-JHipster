import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IKategorie } from 'app/shared/model/kategorie.model';
import { KategorieService } from './kategorie.service';

@Component({
    selector: 'jhi-kategorie-update',
    templateUrl: './kategorie-update.component.html'
})
export class KategorieUpdateComponent implements OnInit {
    private _kategorie: IKategorie;
    isSaving: boolean;

    kategories: IKategorie[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private kategorieService: KategorieService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ kategorie }) => {
            this.kategorie = kategorie;
        });
        this.kategorieService.query().subscribe(
            (res: HttpResponse<IKategorie[]>) => {
                this.kategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.kategorie.id !== undefined) {
            this.subscribeToSaveResponse(this.kategorieService.update(this.kategorie));
        } else {
            this.subscribeToSaveResponse(this.kategorieService.create(this.kategorie));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IKategorie>>) {
        result.subscribe((res: HttpResponse<IKategorie>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackKategorieById(index: number, item: IKategorie) {
        return item.id;
    }
    get kategorie() {
        return this._kategorie;
    }

    set kategorie(kategorie: IKategorie) {
        this._kategorie = kategorie;
    }
}
