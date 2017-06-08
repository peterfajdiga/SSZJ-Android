package peterfajdiga.sszj.logic;

import peterfajdiga.sszj.R;

public final class Sets {
    public static int getSetImageResource(String set) {
        switch (set) {
            case "Osebe":                         return R.drawable.sets_1;
            case "Delo":                          return R.drawable.sets_2;
            case "Opravila":                      return R.drawable.sets_3;
            case "Poklici":                       return R.drawable.sets_4;
            case "Vprašalnice":                   return R.drawable.sets_5;
            case "Osebni zaimki s pomožnikom":    return R.drawable.sets_6;
            case "Čas":                           return R.drawable.sets_7;
            case "Mišljenje":                     return R.drawable.sets_8;
            case "Čustva: volja in občutki":      return R.drawable.sets_9;
            case "Telo":                          return R.drawable.sets_10;
            case "Medicina in bolezni":           return R.drawable.sets_11;
            case "Nega":                          return R.drawable.sets_12;
            case "Narava":                        return R.drawable.sets_13;
            case "Živali":                        return R.drawable.sets_14;
            case "Gibanje":                       return R.drawable.sets_15;
            case "Smer":                          return R.drawable.sets_16;
            case "Potovanje":                     return R.drawable.sets_17;
            case "Bivalni prostori in pohištvo":  return R.drawable.sets_18;
            case "Oblačila":                      return R.drawable.sets_19;
            case "Hrana in pijača":               return R.drawable.sets_20;
            case "Komunikacija":                  return R.drawable.sets_21;
            case "Izobraževanje":                 return R.drawable.sets_22;
            case "Družbena ureditev":             return R.drawable.sets_23;
            case "Sodstvo":                       return R.drawable.sets_24;
            case "Vojna":                         return R.drawable.sets_25;
            case "Denar":                         return R.drawable.sets_26;
            case "Trgovina":                      return R.drawable.sets_27;
            case "Religija":                      return R.drawable.sets_28;
            case "Prosti čas":                    return R.drawable.sets_29;
            case "Šport":                         return R.drawable.sets_30;
            case "Števila":                       return R.drawable.sets_31;
            case "Količina: velikost in stopnja": return R.drawable.sets_32;
            case "Lastnost: vrsta in stanje":     return R.drawable.sets_33;
            case "Države":                        return R.drawable.sets_34;
            case "Mesta":                         return R.drawable.sets_35;
            case "Položaj: lega":                 return R.drawable.sets_36;
            case "Abeceda":                       return R.drawable.sets_37;
            case "Neopredeljeno":                 return R.drawable.sets_38;
            default: return 0;
        }
    }
}
