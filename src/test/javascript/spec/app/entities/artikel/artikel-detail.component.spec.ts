/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArticlesTestModule } from '../../../test.module';
import { ArtikelDetailComponent } from 'app/entities/artikel/artikel-detail.component';
import { Artikel } from 'app/shared/model/artikel.model';

describe('Component Tests', () => {
    describe('Artikel Management Detail Component', () => {
        let comp: ArtikelDetailComponent;
        let fixture: ComponentFixture<ArtikelDetailComponent>;
        const route = ({ data: of({ artikel: new Artikel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [ArtikelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ArtikelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ArtikelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.artikel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
