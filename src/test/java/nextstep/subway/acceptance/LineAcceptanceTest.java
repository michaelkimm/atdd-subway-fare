package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.marker.AcceptanceTest;
import nextstep.utils.AcceptanceTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.steps.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class LineAcceptanceTest {


    /**
     * 지하철노선 생성
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * <p>
     * 노선 생성 시 상행종점역과 하행종점역을 등록합니다.
     * 따라서 이번 단계에서는 지하철 노선에 역을 맵핑하는 기능은 아직 없지만
     * 노선 조회시 포함된 역 목록이 함께 응답됩니다.
     */
    @Test
    void 지하철노선을_생성한다() {
        //when
        int distance = 10;
        int duration = 40;
        ValidatableResponse lineCratedResponse = createLinesWithStations("신분당선", "bg-red-600", "강남역", "언주역", distance, duration);

        //then
        AcceptanceTestUtils.verifyResponseStatus(lineCratedResponse, HttpStatus.CREATED);

        ValidatableResponse foundLineResponse = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(lineCratedResponse));
        AcceptanceTestUtils.verifyResponseStatus(foundLineResponse, HttpStatus.OK);

        verifyFoundLine(foundLineResponse, "신분당선", "bg-red-600", distance, duration, "강남역", "언주역");
    }

    /**
     * 지하철노선 목록 조회
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철노선_목록을_조회한다() {
        //given
        int distance = 10;
        int duration = 40;
        createLinesWithStations("신분당선", "bg-red-600", "강남역", "언주역", distance, duration);
        createLinesWithStations("수인분당선", "bg-green-600", "수원역", "분당역", distance, duration);

        //when
        ValidatableResponse foundLineResponse = AcceptanceTestUtils.getResource(LINES_RESOURCE_URL);

        //then
        AcceptanceTestUtils.verifyResponseStatus(foundLineResponse, HttpStatus.OK);

        verifyFoundLineWithPath("[0]", foundLineResponse, "신분당선", "bg-red-600", distance, duration, "강남역", "언주역");
        verifyFoundLineWithPath("[1]", foundLineResponse, "수인분당선", "bg-green-600", distance, duration, "수원역", "분당역");
    }

    /**
     * 지하철노선 조회
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철노선을_조회한다() {
        //given
        int distance = 10;
        int duration = 40;
        ValidatableResponse lineCratedResponse = createLinesWithStations("신분당선", "bg-red-600", "강남역", "언주역", distance, duration);

        //when
        ValidatableResponse foundLineResponse = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(lineCratedResponse));

        //then
        AcceptanceTestUtils.verifyResponseStatus(foundLineResponse, HttpStatus.OK);

        verifyFoundLine(foundLineResponse, "신분당선", "bg-red-600", distance, duration, "강남역", "언주역");
    }

    /**
     * 지하철노선 수정
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_수정한다() {
        //given
        int distance = 10;
        int duration = 40;
        ValidatableResponse lineCratedResponse = createLinesWithStations("신분당선", "bg-red-600", "강남역", "언주역", distance, duration);

        //when
        ValidatableResponse modifiedLineResponse = modifyLine("바뀐 분당선", "bg-green-600", AcceptanceTestUtils.getLocation(lineCratedResponse));

        //then
        AcceptanceTestUtils.verifyResponseStatus(modifiedLineResponse, HttpStatus.OK);

        ValidatableResponse foundLineResponse = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(lineCratedResponse));
        AcceptanceTestUtils.verifyResponseStatus(foundLineResponse, HttpStatus.OK);
        verifyFoundLine(foundLineResponse, "바뀐 분당선", "bg-green-600", distance, duration, "강남역", "언주역");
    }


    /**
     * 지하철노선 삭제
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_제거한다() {
        //given
        ValidatableResponse lineCratedResponse = createLinesWithStations("신분당선", "bg-red-600", "강남역", "언주역", 10, 40);

        //when
        ValidatableResponse deletedLineResponse = AcceptanceTestUtils.deleteResource(AcceptanceTestUtils.getLocation(lineCratedResponse));

        //then
        AcceptanceTestUtils.verifyResponseStatus(deletedLineResponse, HttpStatus.NO_CONTENT);

        ValidatableResponse foundLineResponse = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(lineCratedResponse));
        AcceptanceTestUtils.verifyResponseStatus(foundLineResponse, HttpStatus.NOT_FOUND);
    }

}