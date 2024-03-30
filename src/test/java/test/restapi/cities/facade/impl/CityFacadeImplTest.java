package test.restapi.cities.facade.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import test.restapi.cities.dto.entityDto.CityDto;
import test.restapi.cities.dto.entityDto.EditCityDto;
import test.restapi.cities.mapper.CityMapper;
import test.restapi.cities.mapper.EditCityMapper;
import test.restapi.cities.model.City;
import test.restapi.cities.service.CityService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CityFacadeImpl.class})
@ExtendWith(MockitoExtension.class)
class CityFacadeImplTest {
    @InjectMocks
    private CityFacadeImpl cityFacadeImpl;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private CityService cityService;

    @Mock
    private EditCityMapper editCityMapper;

    private final String TEST_STRING = "test";

    @Test
    void testGetCityByNameShouldReturnCityDto() {
        City city = createCity();
        when(cityService.getCityByName(any())).thenReturn(city);
        CityDto cityDto = new CityDto();
        when(cityMapper.toDto(any())).thenReturn(cityDto);
        assertSame(cityDto, cityFacadeImpl.getCityByName(TEST_STRING));
        verify(cityService).getCityByName(any());
        verify(cityMapper).toDto(any());
    }


    @Test
    void testGetPageOfCitiesShouldReturnListOfCityDto() {
        when(cityService.getPageOfCities(any())).thenReturn(new ArrayList<>());
        assertTrue(cityFacadeImpl.getPageOfCities(1, 3).isEmpty());
        verify(cityService).getPageOfCities(any());
    }

    @Test
    void testEditCityShouldReturnEditedCityDto() {
        City city = createCity();
        when(cityService.editCityById(any())).thenReturn(city);

        City city1 = createCity();
        EditCityDto editCityDto = new EditCityDto();
        when(editCityMapper.toDto(any())).thenReturn(editCityDto);
        when(editCityMapper.toEntity(any())).thenReturn(city1);
        assertSame(editCityDto, cityFacadeImpl.editCity(new EditCityDto()));
        verify(cityService).editCityById(any());
        verify(editCityMapper).toDto(any());
        verify(editCityMapper).toEntity(any());
    }

    @Test
    void testLoadCityShouldReturnCityDto() {
        City city = createCity();
        when(cityService.loadCity(any())).thenReturn(city);

        City city1 = createCity();
        CityDto cityDto = new CityDto();
        when(cityMapper.toDto(any())).thenReturn(cityDto);
        when(cityMapper.toEntity(any())).thenReturn(city1);
        assertSame(cityDto, cityFacadeImpl.loadCity(new CityDto()));
        verify(cityService).loadCity(any());
        verify(cityMapper).toDto(any());
        verify(cityMapper).toEntity(any());
    }

    private City createCity() {
        City city = new City();
        city.setId(1L);
        city.setName(TEST_STRING);
        city.setImages(new ArrayList<>());
        return city;
    }
}



