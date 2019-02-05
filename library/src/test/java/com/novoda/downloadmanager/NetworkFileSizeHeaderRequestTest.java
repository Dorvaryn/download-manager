package com.novoda.downloadmanager;

import java.io.IOException;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.novoda.downloadmanager.NetworkResponseFixtures.aNetworkResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class NetworkFileSizeHeaderRequestTest {

    private static final NetworkResponse UNSUCCESSFUL_RESPONSE = aNetworkResponse().withSuccessful(false).build();
    private static final NetworkResponse SUCCESSFUL_HEADER_RESPONSE = aNetworkResponse().withHeader("1000").withSuccessful(true).build();
    private static final NetworkResponse SUCCESSFUL_BODY_RESPONSE = aNetworkResponse().withBodyContentLength(2000).withSuccessful(true).build();
    private static final String ANY_RAW_URL = "http://example.com";
    private static final int FILE_HEADER_BYTES = 1000;
    private static final int FILE_BODY_BYTES = 2000;

    private final HttpClient httpClient = mock(HttpClient.class);
    private final NetworkRequestCreator requestCreator = new NetworkRequestCreator();
    private final NetworkFileSizeBodyRequest networkFileSizeBodyRequest = new NetworkFileSizeBodyRequest(httpClient, requestCreator);
    private final NetworkFileSizeHeaderRequest fileSizeHeaderRequest = new NetworkFileSizeHeaderRequest(httpClient, requestCreator, networkFileSizeBodyRequest);

    @Test
    public void returnsUnknownSize_whenHttpClientErrors() throws IOException {
        given(httpClient.execute(requestCreator.createFileSizeHeadRequest(ANY_RAW_URL))).willThrow(new IOException("error message"));

        FileSizeResult fileSizeResult = fileSizeHeaderRequest.requestFileSize(ANY_RAW_URL);

        assertThat(fileSizeResult).isEqualTo(FileSizeResult.failure("error message"));
    }

    @Test
    public void returnsUnknownSize_whenResponseIsUnsuccessful() throws IOException {
        given(httpClient.execute(requestCreator.createFileSizeHeadRequest(ANY_RAW_URL))).willReturn(UNSUCCESSFUL_RESPONSE);
        given(httpClient.execute(requestCreator.createFileSizeBodyRequest(ANY_RAW_URL))).willReturn(UNSUCCESSFUL_RESPONSE);

        FileSizeResult fileSizeResult = fileSizeHeaderRequest.requestFileSize(ANY_RAW_URL);

        assertThat(fileSizeResult).isEqualTo(FileSizeResult.failure("File Size Body Request: 'http://example.com' with response code: '200' failed."));
    }

    @Test
    public void returnsHeaderFileSize_whenResponseSuccessful() throws IOException {
        given(httpClient.execute(requestCreator.createFileSizeHeadRequest(ANY_RAW_URL))).willReturn(SUCCESSFUL_HEADER_RESPONSE);

        FileSizeResult fileSizeResult = fileSizeHeaderRequest.requestFileSize(ANY_RAW_URL);

        assertThat(fileSizeResult).isEqualTo(FileSizeResult.success(FileSizeCreator.createFromTotalSize(FILE_HEADER_BYTES)));
    }

    @Test
    public void returnsBodyFileSize_whenHeaderResponseUnsuccessful() throws IOException {
        given(httpClient.execute(requestCreator.createFileSizeHeadRequest(ANY_RAW_URL))).willReturn(UNSUCCESSFUL_RESPONSE);
        given(httpClient.execute(requestCreator.createFileSizeBodyRequest(ANY_RAW_URL))).willReturn(SUCCESSFUL_BODY_RESPONSE);

        FileSizeResult fileSizeResult = fileSizeHeaderRequest.requestFileSize(ANY_RAW_URL);

        assertThat(fileSizeResult).isEqualTo(FileSizeResult.success(FileSizeCreator.createFromTotalSize(FILE_BODY_BYTES)));
    }

}
