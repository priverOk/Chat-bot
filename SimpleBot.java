import java.util.*;
import java.util.regex.*;

public class SimpleBot {

    //QWITHOUTQM при поставленому питанні,яке бот не розумє і дає заготовлену відповідь
    final String[] QWITHOUTQM = {
            "Ви впевнені в правильності написаного?",
            "Перевірте правильність написаного",
            "Нажаль ви щось написали не так"};

    //QWITHQM використовується,якщо користувач використав в своєму запитанні ?,але у бота немає заготовки для відповіді
    final String[] QWITHQM = {
            "Я не впевнений, що маю таку інформацію",
            "Я не впевнений, що маю такі дані"};
    final Map<String, String> PATTERNS_FOR_ANALYSIS = new HashMap<String, String>() {{
        // вітання
        put("хай", "hello");
        put("ку", "hello");
        put("привіт", "hello");
        put("добрий день", "hello");
        put("вітаю", "hello");
        put("доброго ранку", "hello");
        put("доброго вечора", "hello");
        // прощання
        put("на все добре", "bye");
        put("до побачення", "bye");
        // адреса
        put("де я", "address");
        put("адреса", "address");
        put("яка адреса", "address");
        put("де тц", "address");
        // час
        put("час", "time");
        put("котра\\s.*година", "time");
        put("скільки\\s.*часу", "time");
        put("скільки\\s.*годин", "time");
        // назва тц
        put("як\\s.*називається", "name");
        put("імя", "name");
        put("назва", "name");
        put("яка\\s.*назва", "name");
        // wc
        put("wc", "wc");
        put("туалет", "wc");
        put("вбиральня", "wc");
        // emergency exit
        put("аварійний вихід", "emergency_exit");
        // shops 1 floor go straight
        put("ostin", "s1fs");
        put("остін", "s1fs");
        put("bershka", "s1fs");
        put("бершка", "s1fs");
        put("crocs", "s1fs");
        put("крокс", "s1fs");
        put("mango", "s1fs");
        put("магазин манго", "s1fs");
        put("манго", "s1fs");
        put("kfc", "s1fs");
        put("кфс", "s1fs");
        put("sinsay", "s1fs");
        put("сінсей", "s1fs");
        // shops 1 floor go left
        put("rozetka", "s1fl");
        put("магазин розетка", "s1fl");
        put("розетка", "s1fl");
        put("reserved", "s1fl");
        put("ресервед", "s1fl");
        put("columbia", "s1fl");
        put("колумбія", "s1fl");
        put("silpo", "s1fl");
        put("сільпо", "s1fl");
        // shops 1 floor go right
        put("lavis", "s1fr");
        put("лавіс", "s1fr");
        put("olko", "s1fr");
        put("олко", "s1fr");
        put("odji", "s1fr");
        put("оджі", "s1fr");
        put("кіт і пес", "s1fr");
        put("colins", "s1fr");
        put("колінс", "s1fr");
        // shops 2 floor go straight
        put("LC Waikiki", "s2fs");
        put("вайкікі", "s2fs");
        put("4f", "s2fs");
        put("4ф", "s2fs");
        put("allo", "s2fs");
        put("алло", "s2fs");
        put("sela", "s2fs");
        put("села", "s2fs");
        // shops 2 floor go left
        put("look", "s2fl");
        put("лук", "s2fl");
        put("house", "s2fl");
        put("хаус", "s2fl");
        put("atb", "s2fl");
        put("атб", "s2fl");
        put("allegro", "s2fl");
        put("алегро", "s2fl");
        // shops 2 floor go right
        put("conte", "s2fr");
        put("конте", "s2fr");
        put("terrasse", "s2fr");
        put("терассе", "s2fr");
        put("yanina", "s2fr");
        put("яніна", "s2fr");
        put("eclipse", "s2fr");
        put("екліпс", "s2fr");
        // help (уда обратися за помощю)
        put("крадіжка", "help");
        put("крадій", "help");
        put("грабіж", "help");
        put("пограбування", "help");

    }};
    final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        put("hello", "Здрастуйте, радий вас бачити чим можу допомогти?");
        put("bye", "До побачення. Сподіваюся, я вам допоміг");
        put("address", "Ви в ТРЦ Cosmo вул.Шевченка 12");
        put("name", "ТРЦ Cosmo");
        put("emergency_exit", "Перший поверх біля KFC");
        put("wc", "Перший поверх біля KFC,другий поверх біля АТБ");
        put("s1fs", "Вам потрібно на перший поверех та прямо");
        put("s1fl", "Вам потрібно на перший поверех та на ліво");
        put("s1fr", "Вам потрібно на перший поверех та на право");
        put("s2fs", "Вам потрібно на другий поверех та прямо");
        put("s2fl", "Вам потрібно на другий поверех та на ліво");
        put("s2fr", "Вам потрібно на другий поверех та на право");
        put("help", "Вам можуть допомогти в security room при вході");
    }};

    Pattern pattern;
    Random random;
    Date date;

    public SimpleBot() {
        random = new Random();
        date = new Date();
    }
    public String sayInReturn(String msg, boolean ai) {
        String say = (msg.trim().endsWith("?"))? //перевірка повідомлень з ? для відправлення ботом повідомлень
                QWITHQM[random.nextInt(QWITHQM.length)]: //рандомна відповідь,якщо бот не розуміє питання
                QWITHOUTQM[random.nextInt(QWITHOUTQM.length)];

        if (ai) {
            String message = String.join(" ", msg.toLowerCase().split(" ,.?"));
            //переводимо всі ведені букви в нижній регістр та прибираємо знаки ( ,.?),
            for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) { //перебір шаблонів для аналізу
                pattern = Pattern.compile(o.getKey()); //Компіляція шаблону
                if (pattern.matcher(message).find()) //програма перевіряє,чи ведений текст користувачем є в шаблонах програми
                    if (o.getValue().equals("time")) return date.toString(); //для запиттаня часу
                    else return ANSWERS_BY_PATTERNS.get(o.getValue());
            }
        }
        return say;

    }
}