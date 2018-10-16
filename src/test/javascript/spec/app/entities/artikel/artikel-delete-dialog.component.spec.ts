/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ArticlesTestModule } from '../../../test.module';
import { ArtikelDeleteDialogComponent } from 'app/entities/artikel/artikel-delete-dialog.component';
import { ArtikelService } from 'app/entities/artikel/artikel.service';

describe('Component Tests', () => {
    describe('Artikel Management Delete Component', () => {
        let comp: ArtikelDeleteDialogComponent;
        let fixture: ComponentFixture<ArtikelDeleteDialogComponent>;
        let service: ArtikelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticlesTestModule],
                declarations: [ArtikelDeleteDialogComponent]
            })
                .overrideTemplate(ArtikelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ArtikelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ArtikelService);
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
