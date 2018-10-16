import { Moment } from 'moment';
import { ITag } from 'app/shared/model//tag.model';

export const enum Verfuegbarkeit {
    VORRAETIG = 'VORRAETIG',
    AUSVERKAUFT = 'AUSVERKAUFT',
    BESTELLT = 'BESTELLT'
}

export interface IArtikel {
    id?: number;
    artikelnummer?: string;
    beschreibung?: string;
    titel?: string;
    preis?: number;
    eingestelltAm?: Moment;
    verfuegbarkeit?: Verfuegbarkeit;
    kategorieId?: number;
    kategorieTitel?: string;
    tags?: ITag[];
}

export class Artikel implements IArtikel {
    constructor(
        public id?: number,
        public artikelnummer?: string,
        public beschreibung?: string,
        public titel?: string,
        public preis?: number,
        public eingestelltAm?: Moment,
        public verfuegbarkeit?: Verfuegbarkeit,
        public kategorieId?: number,
        public kategorieTitel?: string,
        public tags?: ITag[]
    ) {}
}
