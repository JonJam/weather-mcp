package com.jonjam.accuweathermcp.locations.textsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.jonjam.accuweathermcp.locations.common.AccuWeatherCountryDto;
import com.jonjam.accuweathermcp.locations.common.LocationSuggestionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LocationTextSearchMapper")
class LocationTextSearchMapperTest {

  private final LocationTextSearchMapper mapper = new LocationTextSearchMapper();

  @Nested
  @DisplayName("toLocationSuggestionDto")
  class ToLocationSuggestionDto {

    @Test
    @DisplayName("maps all fields from AccuWeather location search DTO")
    void mapsAllFieldsFromAccuWeatherLocationSearchDto() {
      // Arrange
      final AccuWeatherCountryDto countryDto =
          AccuWeatherCountryDto.builder().id("US").localizedName("United States").build();

      final AccuWeatherLocationSearchResultDto accuWeatherDto =
          AccuWeatherLocationSearchResultDto.builder()
              .key("12345")
              .type("City")
              .localizedName("San Francisco")
              .country(countryDto)
              .build();

      // Act
      final LocationSuggestionDto result = mapper.toLocationSuggestionDto(accuWeatherDto);

      // Assert
      assertThat(result.getId(), is("12345"));
      assertThat(result.getLocalizedName(), is("San Francisco"));
      assertThat(result.getCountryKey(), is("US"));
      assertThat(result.getCountryLocalizedName(), is("United States"));
    }
  }
}
