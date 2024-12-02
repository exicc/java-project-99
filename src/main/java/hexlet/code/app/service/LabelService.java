package hexlet.code.app.service;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;


    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LabelDTO getLabelById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));
        return labelMapper.toDTO(label);
    }

    public LabelDTO createLabel(LabelCreateDTO labelCreateDTO) {
        if (labelRepository.existsByName(labelCreateDTO.getName())) {
            throw new IllegalArgumentException("Label with the same name already exists");
        }

        Label label = labelMapper.toEntity(labelCreateDTO);
        label = labelRepository.save(label);
        return labelMapper.toDTO(label);
    }

    public LabelDTO updateLabel(Long id, LabelUpdateDTO labelUpdateDTO) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + "not found"));
        labelMapper.partialUpdate(labelUpdateDTO, label);
        label = labelRepository.save(label);
        return labelMapper.toDTO(label);
    }

    public void deleteLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new EntityNotFoundException("Label with id " + labelId + " not found"));

        if (!label.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete a label that is linked to tasks");
        }

        labelRepository.delete(label);
    }
}
