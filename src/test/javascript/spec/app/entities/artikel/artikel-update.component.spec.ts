/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ArticlesTestModule } from '../../../test.module';
import { ArtikelUpdateComponent } from 'app/entities/artikel/artikel-update.component';
import { ArtikelService } from 'app/entities/artikel/artikel.service';
import { Artikel } from 'app/shared/model/artikel.model';

describe('Component Tests', () => {
    describe('Artikel Management Update Component', () => {
        let comp: ArtikelUpdateComponent;
        let fixture: ComponentFixture<ArtikelUpdateComponent>;
        let service: ArtikelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [ArtikelUpdateComponent]
            })
                .overrideTemplate(ArtikelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ArtikelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ArtikelService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Artikel(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.artikel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Artikel();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.artikel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
