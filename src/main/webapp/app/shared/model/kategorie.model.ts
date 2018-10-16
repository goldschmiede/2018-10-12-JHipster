import { IArtikel } from 'app/shared/model//artikel.model';
import { IKategorie } from 'app/shared/model//kategorie.model';

export interface IKategorie {
    id?: number;
    beschreibung?: string;
    titel?: string;
    ueberkategorieId?: number;
    artikels?: IArtikel[];
    unterkategoriens?: IKategorie[];
}

export class Kategorie implements IKategorie {
    constructor(
        public id?: number,
        public beschreibung?: string,
        public titel?: string,
        public ueberkategorieId?: number,
        public artikels?: IArtikel[],
        public unterkategoriens?: IKategorie[]
    ) {}
}
