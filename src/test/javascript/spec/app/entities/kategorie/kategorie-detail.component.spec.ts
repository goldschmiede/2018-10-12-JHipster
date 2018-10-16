/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArticlesTestModule } from '../../../test.module';
import { KategorieDetailComponent } from 'app/entities/kategorie/kategorie-detail.component';
import { Kategorie } from 'app/shared/model/kategorie.model';

describe('Component Tests', () => {
    describe('Kategorie Management Detail Component', () => {
        let comp: KategorieDetailComponent;
        let fixture: ComponentFixture<KategorieDetailComponent>;
        const route = ({ data: of({ kategorie: new Kategorie(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [KategorieDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(KategorieDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(KategorieDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.kategorie).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
