package bg.nbu.logistics.commons.utils;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.modelmapper.ModelMapper;

public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <R> List<R> mapCollection(List<?> collection, Class<R> resultClass) {
        return collection.stream()
                .map(item -> modelMapper.map(item, resultClass))
                .collect(toUnmodifiableList());
    }
}
