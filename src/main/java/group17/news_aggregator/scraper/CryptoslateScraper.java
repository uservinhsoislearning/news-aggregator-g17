package group17.news_aggregator.scraper;

import group17.news_aggregator.exception.EmptyContentException;
import group17.news_aggregator.news.Article;
import group17.news_aggregator.news.News;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CryptoslateScraper extends ArticleScraper {

    @Override
    public List<Article> scrapeAll() throws InterruptedException {
        List<Article> resultList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        final int MAX_RETRIES = 5;

        pageLoop:
        for (int i = 1; i <= getMaxPage(); i++) {
            int retryCount = 0;
            boolean success = false;

            while (retryCount < MAX_RETRIES && !success) {
                try {

                    String url = String.format("https://cryptoslate.com/news/page/%d/", i);

                    Document document = Jsoup
                            .connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:122.0) Gecko/20100101 Firefox/122.0")
                            .get();
                    Elements links = document
                            .body()
                            .select("div[class=\"list-feed slate\"]")
                            .select("div[class=\"list-post\"]")
                            .select("a[href]");


                    for (Element link : links) {
                        executorService.execute(
                                () -> {
                                    String articleUrl = link.attr("abs:href");
                                    Article article = scrapeFromURL(articleUrl);
                                    resultList.add(article);
                                }
                        );
                    }

                    System.out.printf("Page %d scraped. ", i);
                    System.out.println("Number of links: " + links.size());

                    success = true;


                } catch (HttpStatusException httpStatusException) {
                    int statusCode = httpStatusException.getStatusCode();
                    if (statusCode == 404) {
                        break pageLoop;
                    } else {
                        retryCount++;
                        System.out.printf("Error fetching page %d. Retrying (%d/%d)...\n", i, retryCount, MAX_RETRIES);
                    }
                } catch (IOException e) {
                    retryCount++;
                    System.out.printf("Error fetching page %d. Retrying (%d/%d)...\n", i, retryCount, MAX_RETRIES);
                }
            }

        }

        System.out.println("-----------------");
        System.out.println("Finished scraping");
        System.out.println("-----------------");


        executorService.shutdown();
        boolean ignored = executorService.awaitTermination(300, TimeUnit.SECONDS);
        return resultList;

    }

    @Override
    public void getInfoFromURL(String url, News news) throws IOException {

        news.setLink(url);
        news.setWebsiteSource("Cryptoslate");

        Document document = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:122.0) Gecko/20100101 Firefox/122.0")
                .get();

        //get tags
        try {
            Elements tags = document
                    .body()
                    .select("div[class=\"news-cats\"]")
                    .select("a[rel=\"tag\"]");
            List<String> tagsList = tags.eachText();
            news.setTags(tagsList);
        } catch (NoSuchElementException ignored) {
        }

        //get category
        try {
            Elements category = document
                    .body()
                    .select("div[class=\"news-cats\"]")
                    .select("span")
                    .select("a[href]");
            news.setCategory(category.text());
        } catch (NoSuchElementException ignored) {
        }


        // get author
        String author = document
                .head()
                .select("meta[name=\"author\"]")
                .attr("content");
        if (author.isEmpty()) {
            author = "Guest";
        }
        news.setAuthor(author);

        //get summary (description)
        String summary = document
                .head()
                .select("meta[property=\"og:description\"]")
                .attr("content");
        news.setSummary(summary);

        // get title
        String title = document
                .head()
                .select("meta[property=\"og:title\"]")
                .attr("content");
        news.setTitle(title);


        // get content
        try {
            List<String> content = document
                    .select("article[class=\"full-article\"]")
                    .select("p")
                    .eachText();
            news.setContent(content);
        } catch (NoSuchElementException e) {
            throw new EmptyContentException(news.getLink());
        }


        // get creation date
        String creationDate = document
                .body()
                .select("div[class=\"post-date\"]")
                .text();
        if (creationDate.isEmpty()) {
            creationDate = "Jan. 1, 2000";
        }
        news.setCreationDate(creationDate);


    }
}
