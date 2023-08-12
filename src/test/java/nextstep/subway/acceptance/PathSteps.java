package nextstep.subway.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}", source, target)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청하는_문서(RequestSpecification spec, Long source, Long target) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}", source, target)
			.then().log().all().extract();
	}
}
