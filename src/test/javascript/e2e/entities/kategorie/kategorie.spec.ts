/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { KategorieComponentsPage, KategorieDeleteDialog, KategorieUpdatePage } from './kategorie.page-object';

const expect = chai.expect;

describe('Kategorie e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let kategorieUpdatePage: KategorieUpdatePage;
    let kategorieComponentsPage: KategorieComponentsPage;
    let kategorieDeleteDialog: KategorieDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Kategories', async () => {
        await navBarPage.goToEntity('kategorie');
        kategorieComponentsPage = new KategorieComponentsPage();
        expect(await kategorieComponentsPage.getTitle()).to.eq('articlesApp.kategorie.home.title');
    });

    it('should load create Kategorie page', async () => {
        await kategorieComponentsPage.clickOnCreateButton();
        kategorieUpdatePage = new KategorieUpdatePage();
        expect(await kategorieUpdatePage.getPageTitle()).to.eq('articlesApp.kategorie.home.createOrEditLabel');
        await kategorieUpdatePage.cancel();
    });

    it('should create and save Kategories', async () => {
        const nbButtonsBeforeCreate = await kategorieComponentsPage.countDeleteButtons();

        await kategorieComponentsPage.clickOnCreateButton();
        await kategorieUpdatePage.setBeschreibungInput('beschreibung');
        expect(await kategorieUpdatePage.getBeschreibungInput()).to.eq('beschreibung');
        await kategorieUpdatePage.setTitelInput('titel');
        expect(await kategorieUpdatePage.getTitelInput()).to.eq('titel');
        await kategorieUpdatePage.ueberkategorieSelectLastOption();
        await kategorieUpdatePage.save();
        expect(await kategorieUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await kategorieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Kategorie', async () => {
        const nbButtonsBeforeDelete = await kategorieComponentsPage.countDeleteButtons();
        await kategorieComponentsPage.clickOnLastDeleteButton();

        kategorieDeleteDialog = new KategorieDeleteDialog();
        expect(await kategorieDeleteDialog.getDialogTitle()).to.eq('articlesApp.kategorie.delete.question');
        await kategorieDeleteDialog.clickOnConfirmButton();

        expect(await kategorieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
