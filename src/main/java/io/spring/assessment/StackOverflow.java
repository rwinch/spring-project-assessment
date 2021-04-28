package io.spring.assessment;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public class StackOverflow {
	private final WebClient webClient;

	public StackOverflow() {
		this("https://api.stackexchange.com");
	}

	public StackOverflow(String baseUrl) {
		this.webClient = WebClient.builder().baseUrl(baseUrl).build();
	}

	public Mono<Long> findQuestionCountForTagInMonth(String tag, YearMonth yearMonth) {
		int year = yearMonth.getYear();
		String month = String.format("%02d", yearMonth.getMonthValue());
		int lastDayOfMonth = yearMonth.lengthOfMonth();
		return this.webClient
				.get()
				.uri("/2.2/search?site=stackoverflow&tagged={tag}&filter=total&fromdate={year}-{month}-01&todate={year}-{month}-{lastDayOfMonth}", tag, year, month, year, month, lastDayOfMonth)
				.retrieve()
				.bodyToMono(Total.class)
				.map(Total::getTotal);
	}

	static class Total {
		private long total;

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}
	}
}
