package ru.otus.pro.servises;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.pro.dto.BoardCreateDTO;
import ru.otus.pro.entities.BulletinBoard;
import ru.otus.pro.exceptions.NotFoundException;
import ru.otus.pro.exceptions.ServiceException;
import ru.otus.pro.exceptions.ValidationException;
import ru.otus.pro.repositories.BulletinBoardRepository;
import ru.otus.pro.services.BoardsServiceImpl;
import ru.otus.pro.services.BulletinService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BoardsServiceTest {
    @Mock
    private BulletinBoardRepository bulletinBoardRepository;
    @Mock
    private BulletinService bulletinService;
    @InjectMocks
    private BoardsServiceImpl boardsService;

    @Test
    void create_boardValidation_success() {
        var newBoard = new BoardCreateDTO();
        newBoard.setName("New name");
        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        savedBoard.setName("saved name");

        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);
        Mockito.when(bulletinBoardRepository.save(Mockito.any()))
                .thenReturn(savedBoard);

        var result = boardsService.create("manager", newBoard);
        Assertions.assertThat(result)
                .isEqualTo(savedBoard.getId());
    }

    @Test
    void create_boardValidation_nameNotUnique() {
        var newBoard = new BoardCreateDTO();
        newBoard.setName("New name");
        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        savedBoard.setName("saved name");
        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(true);

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.create("manager", newBoard);
        });
        Assertions.assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void create_board_saveError() {
        var newBoard = new BoardCreateDTO();
        newBoard.setName("Test name");
        newBoard.setDescription("Test description");
        newBoard.setDisabled(false);

        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);

        Mockito.when(bulletinBoardRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException());

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.create("manager", newBoard);
        });
        Assertions.assertThat(thrown).isInstanceOf(ServiceException.class);
    }

    @Test
    void update_board_saveError() {
        var id = 1L;
        var newBoard = new BoardCreateDTO();
        newBoard.setName("New name");
        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        savedBoard.setName("saved name");
        Mockito.when(bulletinBoardRepository.findById(savedBoard.getId()))
                .thenReturn(Optional.of(savedBoard));
        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);
        Mockito.when(bulletinBoardRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException());

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.update(id, newBoard);
        });
        Assertions.assertThat(thrown).isInstanceOf(ServiceException.class);
    }

    @Test
    void update_board_success() {
        var id = 1L;
        var newBoard = new BoardCreateDTO();
        newBoard.setName("New name");
        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        savedBoard.setName("saved name");
        Mockito.when(bulletinBoardRepository.findById(savedBoard.getId()))
                .thenReturn(Optional.of(savedBoard));
        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);
        Mockito.when(bulletinBoardRepository.save(Mockito.any()))
                .thenReturn(savedBoard);

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.update(id, newBoard);
        });
        Assertions.assertThat(thrown).isNull();
    }


    @Test
    void update_boardValidation_nameNotUnique() {
        var newBoard = new BoardCreateDTO();
        newBoard.setName("Test name");
        newBoard.setDescription("Test description");
        newBoard.setDisabled(false);

        var savedBoard = new BulletinBoard();
        savedBoard.setId(1L);
        Mockito.when(bulletinBoardRepository.findById(savedBoard.getId()))
                .thenReturn(Optional.of(savedBoard));
        Mockito.when(bulletinBoardRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(true);

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.update(1L, newBoard);
        });
        Assertions.assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void update_board_notFound() {
        var id = 1L;
        var newBoard = new BoardCreateDTO();

        Mockito.when(bulletinBoardRepository.findById(id))
                .thenReturn(Optional.empty());

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.update(id, newBoard);
        });
        Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_board_linkedItemsExistsError() {
        var id = 1L;
        Mockito.when(bulletinService.exists(id))
                .thenReturn(true);

        var thrown = Assertions.catchThrowable(() -> {
            boardsService.delete(id);
        });
        Assertions.assertThat(thrown).isInstanceOf(ValidationException.class);
    }

}