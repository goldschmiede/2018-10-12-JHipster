import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IArtikel } from 'app/shared/model/artikel.model';
import { ArtikelService } from './artikel.service';
import { IKategorie } from 'app/shared/model/kategorie.model';
import { KategorieService } from 'app/entities/kategorie';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-artikel-update',
    templateUrl: './artikel-update.component.html'
})
export class ArtikelUpdateComponent implements OnInit {
    private _artikel: IArtikel;
    isSaving: boolean;

    kategories: IKategorie[];

    tags: ITag[];
    eingestelltAmDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private artikelService: ArtikelService,
        private kategorieService: KategorieService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ artikel }) => {
            this.artikel = artikel;
        });
        this.kategorieService.query().subscribe(
            (res: HttpResponse<IKategorie[]>) => {
                this.kategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.artikel.id !== undefined) {
            this.subscribeToSaveResponse(this.artikelService.update(this.artikel));
        } else {
            this.subscribeToSaveResponse(this.artikelService.create(this.artikel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IArtikel>>) {
        result.subscribe((res: HttpResponse<IArtikel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get artikel() {
        return this._artikel;
    }

    set artikel(artikel: IArtikel) {
        this._artikel = artikel;
    }
}
