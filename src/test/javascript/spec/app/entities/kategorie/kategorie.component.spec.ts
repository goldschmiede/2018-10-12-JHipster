/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ArticlesTestModule } from '../../../test.module';
import { KategorieComponent } from 'app/entities/kategorie/kategorie.component';
import { KategorieService } from 'app/entities/kategorie/kategorie.service';
import { Kategorie } from 'app/shared/model/kategorie.model';

describe('Component Tests', () => {
    describe('Kategorie Management Component', () => {
        let comp: KategorieComponent;
        let fixture: ComponentFixture<KategorieComponent>;
        let service: KategorieService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [KategorieComponent],
                providers: []
            })
                .overrideTemplate(KategorieComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(KategorieComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(KategorieService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Kategorie(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.kategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
