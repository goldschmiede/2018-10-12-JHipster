import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IKategorie } from 'app/shared/model/kategorie.model';

type EntityResponseType = HttpResponse<IKategorie>;
type EntityArrayResponseType = HttpResponse<IKategorie[]>;

@Injectable({ providedIn: 'root' })
export class KategorieService {
    private resourceUrl = SERVER_API_URL + 'api/kategories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/kategories';

    constructor(private http: HttpClient) {}

    create(kategorie: IKategorie): Observable<EntityResponseType> {
        return this.http.post<IKategorie>(this.resourceUrl, kategorie, { observe: 'response' });
    }

    update(kategorie: IKategorie): Observable<EntityResponseType> {
        return this.http.put<IKategorie>(this.resourceUrl, kategorie, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IKategorie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IKategorie[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IKategorie[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
