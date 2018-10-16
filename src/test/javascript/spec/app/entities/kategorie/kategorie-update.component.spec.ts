/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ArticlesTestModule } from '../../../test.module';
import { KategorieUpdateComponent } from 'app/entities/kategorie/kategorie-update.component';
import { KategorieService } from 'app/entities/kategorie/kategorie.service';
import { Kategorie } from 'app/shared/model/kategorie.model';

describe('Component Tests', () => {
    describe('Kategorie Management Update Component', () => {
        let comp: KategorieUpdateComponent;
        let fixture: ComponentFixture<KategorieUpdateComponent>;
        let service: KategorieService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [KategorieUpdateComponent]
            })
                .overrideTemplate(KategorieUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(KategorieUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(KategorieService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Kategorie(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.kategorie = entity;
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
                    const entity = new Kategorie();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.kategorie = entity;
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
