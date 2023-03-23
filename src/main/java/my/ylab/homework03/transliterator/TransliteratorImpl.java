package my.ylab.homework03.transliterator;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {
    public static final Map<Character, String> MAP;

    static
    {
        Hashtable<Character,String> tmp =
                new Hashtable<>();
        tmp.put('А', "A");
        tmp.put('Б', "B");
        tmp.put('В', "V");
        tmp.put('Г', "G");
        tmp.put('Д', "D");
        tmp.put('Е', "E");
        tmp.put('Ё', "E");
        tmp.put('Ж', "ZH");
        tmp.put('З', "Z");
        tmp.put('И', "I");
        tmp.put('Й', "I");
        tmp.put('К', "K");
        tmp.put('Л', "L");
        tmp.put('М', "M");
        tmp.put('Н', "N");
        tmp.put('О', "O");
        tmp.put('П', "P");
        tmp.put('Р', "R");
        tmp.put('С', "S");
        tmp.put('Т', "T");
        tmp.put('У', "U");
        tmp.put('Ф', "F");
        tmp.put('Х', "KH");
        tmp.put('Ц', "TS");
        tmp.put('Ч', "CH");
        tmp.put('Ш', "SH");
        tmp.put('Щ', "SHCH");
        tmp.put('Ъ', "IE");
        tmp.put('Ы', "Y");
        tmp.put('Ь', "");
        tmp.put('Э', "E");
        tmp.put('Ю', "IU");
        tmp.put('Я', "IA");
        MAP = Collections.unmodifiableMap(tmp);
    }

    @Override
    public String transliterate(String source) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            char symbolCode = source.charAt(i);
            if (MAP.containsKey(symbolCode)) {
                sb.append(MAP.get(symbolCode));
            }
            else {
                sb.append(symbolCode);
            }
        }
        return sb.toString();
    }
}
