import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IArtikel } from 'app/shared/model/artikel.model';

type EntityResponseType = HttpResponse<IArtikel>;
type EntityArrayResponseType = HttpResponse<IArtikel[]>;

@Injectable({ providedIn: 'root' })
export class ArtikelService {
    private resourceUrl = SERVER_API_URL + 'api/artikels';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/artikels';

    constructor(private http: HttpClient) {}

    create(artikel: IArtikel): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(artikel);
        return this.http
            .post<IArtikel>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(artikel: IArtikel): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(artikel);
        return this.http
            .put<IArtikel>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IArtikel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArtikel[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArtikel[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(artikel: IArtikel): IArtikel {
        const copy: IArtikel = Object.assign({}, artikel, {
            eingestelltAm:
                artikel.eingestelltAm != null && artikel.eingestelltAm.isValid() ? artikel.eingestelltAm.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.eingestelltAm = res.body.eingestelltAm != null ? moment(res.body.eingestelltAm) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((artikel: IArtikel) => {
            artikel.eingestelltAm = artikel.eingestelltAm != null ? moment(artikel.eingestelltAm) : null;
        });
        return res;
    }
}
