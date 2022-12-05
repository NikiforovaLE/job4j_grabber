package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HarbCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        Connection connection = Jsoup.connect(PAGE_LINK);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = Objects.requireNonNull(titleElement).child(0);
            Element dateElement = row.select(".vacancy-card__date").first();
            Element dateTime = Objects.requireNonNull(dateElement).child(0);
            String vacancyName = titleElement.text();
            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String date = dateTime.attr("datetime");
            HarbCareerDateTimeParser harbCareerDateTimeParser = new HarbCareerDateTimeParser();
            LocalDateTime time = harbCareerDateTimeParser.parse(date);
            System.out.printf("%s %s, date of the vacancy: %s%n", vacancyName, link, time);
        });
    }
}
