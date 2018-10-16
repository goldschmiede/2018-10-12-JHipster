/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ArtikelComponentsPage, ArtikelDeleteDialog, ArtikelUpdatePage } from './artikel.page-object';

const expect = chai.expect;

describe('Artikel e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let artikelUpdatePage: ArtikelUpdatePage;
    let artikelComponentsPage: ArtikelComponentsPage;
    let artikelDeleteDialog: ArtikelDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Artikels', async () => {
        await navBarPage.goToEntity('artikel');
        artikelComponentsPage = new ArtikelComponentsPage();
        expect(await artikelComponentsPage.getTitle()).to.eq('articlesApp.artikel.home.title');
    });

    it('should load create Artikel page', async () => {
        await artikelComponentsPage.clickOnCreateButton();
        artikelUpdatePage = new ArtikelUpdatePage();
        expect(await artikelUpdatePage.getPageTitle()).to.eq('articlesApp.artikel.home.createOrEditLabel');
        await artikelUpdatePage.cancel();
    });

    it('should create and save Artikels', async () => {
        const nbButtonsBeforeCreate = await artikelComponentsPage.countDeleteButtons();

        await artikelComponentsPage.clickOnCreateButton();
        await artikelUpdatePage.setArtikelnummerInput('artikelnummer');
        expect(await artikelUpdatePage.getArtikelnummerInput()).to.eq('artikelnummer');
        await artikelUpdatePage.setBeschreibungInput('beschreibung');
        expect(await artikelUpdatePage.getBeschreibungInput()).to.eq('beschreibung');
        await artikelUpdatePage.setTitelInput('titel');
        expect(await artikelUpdatePage.getTitelInput()).to.eq('titel');
        await artikelUpdatePage.setPreisInput('5');
        expect(await artikelUpdatePage.getPreisInput()).to.eq('5');
        await artikelUpdatePage.setEingestelltAmInput('2000-12-31');
        expect(await artikelUpdatePage.getEingestelltAmInput()).to.eq('2000-12-31');
        await artikelUpdatePage.verfuegbarkeitSelectLastOption();
        await artikelUpdatePage.kategorieSelectLastOption();
        // artikelUpdatePage.tagsSelectLastOption();
        await artikelUpdatePage.save();
        expect(await artikelUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await artikelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Artikel', async () => {
        const nbButtonsBeforeDelete = await artikelComponentsPage.countDeleteButtons();
        await artikelComponentsPage.clickOnLastDeleteButton();

        artikelDeleteDialog = new ArtikelDeleteDialog();
        expect(await artikelDeleteDialog.getDialogTitle()).to.eq('articlesApp.artikel.delete.question');
        await artikelDeleteDialog.clickOnConfirmButton();

        expect(await artikelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
