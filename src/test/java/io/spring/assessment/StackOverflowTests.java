package io.spring.assessment;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.YearMonth;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class StackOverflowTests {
	StackOverflow stackOverflow = new StackOverflow();

	MockWebServer server;

	String baseUrl;

	@BeforeEach
	void before() {
		this.server = new MockWebServer();
		this.baseUrl = this.server.url("").toString();
		this.stackOverflow = new StackOverflow(this.baseUrl);
	}

	@AfterEach
	void after() throws IOException {
		this.server.shutdown();
	}

	@Test
	void findQuestionCountForTagInMonthWhenSingleDigitMonth() throws Exception {
		this.server.enqueue(new MockResponse()
			.setBody("{\n" +
			"    \"total\": 204\n" +
			"}\n")
			.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2021, 4)).block();

		assertThat(count).isEqualTo(204);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2021-04-01&todate=2021-04-30");
	}

	@Test
	void findQuestionCountForTagInMonthWhenDoubleDigitMonth() throws Exception {
		this.server.enqueue(new MockResponse()
				.setBody("{\n" +
						"    \"total\": 10\n" +
						"}\n")
				.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2022, 10)).block();

		assertThat(count).isEqualTo(10);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2022-10-01&todate=2022-10-31");
	}

	@Test
	void findQuestionCountForTagInMonthWhen31DaysInMonth() throws Exception {
		this.server.enqueue(new MockResponse()
				.setBody("{\n" +
						"    \"total\": 31\n" +
						"}\n")
				.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2022, 7)).block();

		assertThat(count).isEqualTo(31);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2022-07-01&todate=2022-07-31");
	}

	@Test
	void findQuestionCountForTagInMonthWhen30DaysInMonth() throws Exception {
		this.server.enqueue(new MockResponse()
				.setBody("{\n" +
						"    \"total\": 31\n" +
						"}\n")
				.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2021, 6)).block();

		assertThat(count).isEqualTo(31);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2021-06-01&todate=2021-06-30");
	}

	@Test
	void findQuestionCountForTagInMonthWhen28DaysInMonth() throws Exception {
		this.server.enqueue(new MockResponse()
				.setBody("{\n" +
						"    \"total\": 31\n" +
						"}\n")
				.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2021, 2)).block();

		assertThat(count).isEqualTo(31);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2021-02-01&todate=2021-02-28");
	}

	@Test
	void findQuestionCountForTagInMonthWhen29DaysInMonth() throws Exception {
		this.server.enqueue(new MockResponse()
				.setBody("{\n" +
						"    \"total\": 31\n" +
						"}\n")
				.addHeader("content-type", "application/json; charset=utf-8")
		);

		long count = stackOverflow.findQuestionCountForTagInMonth("spring-security", YearMonth.of(2024, 2)).block();

		assertThat(count).isEqualTo(31);
		RecordedRequest request = this.server.takeRequest(1, TimeUnit.SECONDS);
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getRequestUrl().url().toString()).isEqualTo(this.baseUrl + "2.2/search?site=stackoverflow&tagged=spring-security&filter=total&fromdate=2024-02-01&todate=2024-02-29");
	}

}