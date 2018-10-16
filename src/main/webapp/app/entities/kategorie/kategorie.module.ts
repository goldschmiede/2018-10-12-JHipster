import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticlesSharedModule } from 'app/shared';
import {
    KategorieComponent,
    KategorieDetailComponent,
    KategorieUpdateComponent,
    KategorieDeletePopupComponent,
    KategorieDeleteDialogComponent,
    kategorieRoute,
    kategoriePopupRoute
} from './';

const ENTITY_STATES = [...kategorieRoute, ...kategoriePopupRoute];

@NgModule({
    imports: [ArticlesSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        KategorieComponent,
        KategorieDetailComponent,
        KategorieUpdateComponent,
        KategorieDeleteDialogComponent,
        KategorieDeletePopupComponent
    ],
    entryComponents: [KategorieComponent, KategorieUpdateComponent, KategorieDeleteDialogComponent, KategorieDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticlesKategorieModule {}
