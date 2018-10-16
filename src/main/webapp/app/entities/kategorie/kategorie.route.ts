import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Kategorie } from 'app/shared/model/kategorie.model';
import { KategorieService } from './kategorie.service';
import { KategorieComponent } from './kategorie.component';
import { KategorieDetailComponent } from './kategorie-detail.component';
import { KategorieUpdateComponent } from './kategorie-update.component';
import { KategorieDeletePopupComponent } from './kategorie-delete-dialog.component';
import { IKategorie } from 'app/shared/model/kategorie.model';

@Injectable({ providedIn: 'root' })
export class KategorieResolve implements Resolve<IKategorie> {
    constructor(private service: KategorieService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((kategorie: HttpResponse<Kategorie>) => kategorie.body));
        }
        return of(new Kategorie());
    }
}

export const kategorieRoute: Routes = [
    {
        path: 'kategorie',
        component: KategorieComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.kategorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'kategorie/:id/view',
        component: KategorieDetailComponent,
        resolve: {
            kategorie: KategorieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.kategorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'kategorie/new',
        component: KategorieUpdateComponent,
        resolve: {
            kategorie: KategorieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.kategorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'kategorie/:id/edit',
        component: KategorieUpdateComponent,
        resolve: {
            kategorie: KategorieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.kategorie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const kategoriePopupRoute: Routes = [
    {
        path: 'kategorie/:id/delete',
        component: KategorieDeletePopupComponent,
        resolve: {
            kategorie: KategorieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articlesApp.kategorie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
