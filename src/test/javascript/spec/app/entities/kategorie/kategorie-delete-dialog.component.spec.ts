/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ArticlesTestModule } from '../../../test.module';
import { KategorieDeleteDialogComponent } from 'app/entities/kategorie/kategorie-delete-dialog.component';
import { KategorieService } from 'app/entities/kategorie/kategorie.service';

describe('Component Tests', () => {
    describe('Kategorie Management Delete Component', () => {
        let comp: KategorieDeleteDialogComponent;
        let fixture: ComponentFixture<KategorieDeleteDialogComponent>;
        let service: KategorieService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [KategorieDeleteDialogComponent]
            })
                .overrideTemplate(KategorieDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(KategorieDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(KategorieService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
