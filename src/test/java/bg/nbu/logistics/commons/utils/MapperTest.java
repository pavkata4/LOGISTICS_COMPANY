package bg.nbu.logistics.commons.utils;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class MapperTest {
    private static final String ANY_STRING_OBJECT = "anyStringObject";

    @InjectMocks
    private Mapper mapper;

    @Mock
    private ModelMapper modelMapperMock;

    @Test
    void testMapCollection() {
        when(modelMapperMock.map(ANY_STRING_OBJECT, String.class)).thenReturn(ANY_STRING_OBJECT);

        assertThat(mapper.mapCollection(singletonList(ANY_STRING_OBJECT), String.class), contains(ANY_STRING_OBJECT));
    }

    @Test
    void testMapEmptyCollection() {
        assertThat(mapper.mapCollection(emptyList(), String.class), empty());
    }
}
