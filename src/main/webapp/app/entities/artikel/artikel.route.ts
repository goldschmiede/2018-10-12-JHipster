import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Artikel } from 'app/shared/model/artikel.model';
import { ArtikelService } from './artikel.service';
import { ArtikelComponent } from './artikel.component';
import { ArtikelDetailComponent } from './artikel-detail.component';
import { ArtikelUpdateComponent } from './artikel-update.component';
import { ArtikelDeletePopupComponent } from './artikel-delete-dialog.component';
import { IArtikel } from 'app/shared/model/artikel.model';

@Injectable({ providedIn: 'root' })
export class ArtikelResolve implements Resolve<IArtikel> {
    constructor(private service: ArtikelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((artikel: HttpResponse<Artikel>) => artikel.body));
        }
        return of(new Artikel());
    }
}

export const artikelRoute: Routes = [
    {
        path: 'artikel',
        component: ArtikelComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'articlesApp.artikel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'artikel/:id/view',
        component: ArtikelDetailComponent,
        resolve: {
            artikel: ArtikelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.artikel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'artikel/new',
        component: ArtikelUpdateComponent,
        resolve: {
            artikel: ArtikelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.artikel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'artikel/:id/edit',
        component: ArtikelUpdateComponent,
        resolve: {
            artikel: ArtikelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.artikel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const artikelPopupRoute: Routes = [
    {
        path: 'artikel/:id/delete',
        component: ArtikelDeletePopupComponent,
        resolve: {
            artikel: ArtikelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.artikel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
