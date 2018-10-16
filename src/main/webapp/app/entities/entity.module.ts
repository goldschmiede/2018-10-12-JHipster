import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ArticlesArtikelModule } from './artikel/artikel.module';
import { ArticlesKategorieModule } from './kategorie/kategorie.module';
import { ArticlesTagModule } from './tag/tag.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ArticlesArtikelModule,
        ArticlesKategorieModule,
        ArticlesTagModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticlesEntityModule {}
