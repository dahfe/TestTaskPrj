package test.restapi.cities.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import test.restapi.cities.exception.NotFoundException;
import test.restapi.cities.exception.ModelExistsException;
import test.restapi.cities.model.City;
import test.restapi.cities.model.Image;
import test.restapi.cities.repository.CityRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CityServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityServiceImpl;

    private final String TEST_STRING = "test";

    @Test
    void getCityByNameShouldReturnCityObject() {
        City city = createCity();
        when(cityRepository.findByName(any())).thenReturn(Optional.of(city));
        City actualCity = cityServiceImpl.getCityByName(TEST_STRING);
        Assertions.assertEquals(city, actualCity);
        verify(cityRepository).findByName(any());
    }

    @Test
    void getCityByNameWhenNotExistsInDBShouldThrowException() {
        when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> cityServiceImpl.getCityByName(TEST_STRING));
        verify(cityRepository).findByName(any());
    }

    @Test
    void getListOfCitiesShouldBeNotNull() {
        when(cityRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        Assertions.assertNotNull(cityServiceImpl.getPageOfCities((any())));
        verify(cityRepository).findAll((Pageable) any());
    }

    @Test
    void testEditCityByIdShouldCompareCityObjects() {
        City city = createCity();
        Image image = createImage(city);

        ArrayList<Image> imageList = new ArrayList<>();
        imageList.add(image);

        City city1 = new City();
        city1.setId(123L);
        city1.setImages(imageList);
        city1.setName(TEST_STRING);

        City city2 = createCity();

        when(cityRepository.save(any())).thenReturn(city2);
        when(cityRepository.findById(any())).thenReturn(Optional.of(city1));

        City city3 = createCity();
        City actualEditCityByIdResult = cityServiceImpl.editCityById(city3);
        Assertions.assertSame(city1, actualEditCityByIdResult);
        Assertions.assertEquals(TEST_STRING, actualEditCityByIdResult.getName());
        Assertions.assertEquals(1, actualEditCityByIdResult.getImages().size());
        Assertions.assertSame(actualEditCityByIdResult, actualEditCityByIdResult.getImages().get(0).getCity());
        Assertions.assertEquals(TEST_STRING, actualEditCityByIdResult.getImages().get(0).getCity().getName());
        verify(cityRepository).save(any());
        verify(cityRepository).findById(any());
    }

    @Test
    void testEditCityByIdWhenNotExistsInDBShouldThrowException() {
        City city = createCity();
        when(cityRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> cityServiceImpl.editCityById(city));
        verify(cityRepository).findById(any());
    }

    @Test
    void testLoadCityByIdWhenCityExistsInDBShouldThrowException() {
        City city1 = createCity();
        when(cityRepository.findByName(any())).thenReturn(Optional.of(city1));
        City city2 = createCity();
        assertThrows(ModelExistsException.class, () -> cityServiceImpl.loadCity(city2));
        verify(cityRepository).findByName(any());
    }

    @Test
    void testLoadCityByIdShouldCompareCityObjects() {
        City city = createCity();
        when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        assertSame(city, cityServiceImpl.loadCity(city));
        verify(cityRepository).save(any());
        verify(cityRepository).findByName(any());
    }

    private City createCity() {
        City city = new City();
        city.setId(1L);
        city.setName(TEST_STRING);
        city.setImages(new ArrayList<>());
        return city;
    }

    private Image createImage(City city) {
        Image image = new Image();
        image.setName(TEST_STRING);
        image.setCity(city);
        image.setId(1L);
        image.setUrl(TEST_STRING);
        return image;
    }
}

