package com.jonjam.weathermcp.locations.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.jonjam.weathermcp.locations.common.AccuWeatherCountryDto;
import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LocationSuggestionMapper")
class LocationSuggestionMapperTest {

  private final LocationSuggestionMapper mapper = new LocationSuggestionMapper();

  @Nested
  @DisplayName("toLocationSuggestionDto")
  class ToLocationSuggestionDto {

    @Test
    @DisplayName("maps all fields from AccuWeather DTO")
    void mapsAllFieldsFromAccuWeatherDto() {
      // Arrange
      final AccuWeatherCountryDto countryDto =
          AccuWeatherCountryDto.builder().id("US").localizedName("United States").build();

      final AccuWeatherLocationsAutocompleteDto accuWeatherDto =
          AccuWeatherLocationsAutocompleteDto.builder()
              .key("12345")
              .localizedName("San Francisco")
              .type("City")
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
