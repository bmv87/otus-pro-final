package ru.otus.pro.servises;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.pro.dto.BulletinCreateDTO;
import ru.otus.pro.dto.BulletinUpdateDTO;
import ru.otus.pro.entities.Bulletin;
import ru.otus.pro.exceptions.NotFoundException;
import ru.otus.pro.exceptions.ServiceException;
import ru.otus.pro.repositories.BulletinRepository;
import ru.otus.pro.services.BoardsService;
import ru.otus.pro.services.BulletinServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BulletinServiceTest {
    @Mock
    private BulletinRepository bulletinRepository;
    @Mock
    private BoardsService boardsService;
    @InjectMocks
    private BulletinServiceImpl bulletinService;

    @Test
    void create_bulletinValidation_success() {
        var newBulletin = new BulletinCreateDTO();
        newBulletin.setBoardId(1L);
        newBulletin.setContent("Test content");
        newBulletin.setTitle("Test content");

        var savedBulletin = new Bulletin();
        savedBulletin.setId(1L);
        Mockito.when(boardsService.exists(newBulletin.getBoardId()))
                .thenReturn(true);
        Mockito.when(bulletinRepository.save(Mockito.any()))
                .thenReturn(savedBulletin);

        var result = bulletinService.create("manager", newBulletin);
        Assertions.assertThat(result)
                .isEqualTo(savedBulletin.getId());
    }

    @Test
    void create_bulletinValidation_board_notFound() {
        var newBulletin = new BulletinCreateDTO();
        newBulletin.setBoardId(1L);

        Mockito.when(boardsService.exists(newBulletin.getBoardId()))
                .thenReturn(false);

        var thrown = Assertions.catchThrowable(() -> {
            bulletinService.create("manager", newBulletin);
        });
        Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_bulletin_saveError() {
        var newBulletin = new BulletinCreateDTO();
        newBulletin.setBoardId(1);

        var savedBulletin = new Bulletin();
        savedBulletin.setId(1L);
        Mockito.when(boardsService.exists(newBulletin.getBoardId()))
                .thenReturn(true);
        Mockito.when(bulletinRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException());

        var thrown = Assertions.catchThrowable(() -> {
            bulletinService.create("manager", newBulletin);
        });
        Assertions.assertThat(thrown).isInstanceOf(ServiceException.class);
    }

    @Test
    void update_bulletin_saveError() {
        var id = 1L;
        var newBulletin = new BulletinUpdateDTO();
        var savedBulletin = new Bulletin();
        savedBulletin.setId(1L);
        Mockito.when(bulletinRepository.findById(id))
                .thenReturn(Optional.of(savedBulletin));
        Mockito.when(bulletinRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException());

        var thrown = Assertions.catchThrowable(() -> {
            bulletinService.update(id, newBulletin);
        });
        Assertions.assertThat(thrown).isInstanceOf(ServiceException.class);
    }

    @Test
    void update_bulletin_notFound() {
        var id = 1L;
        var newBulletin = new BulletinUpdateDTO();

        Mockito.when(bulletinRepository.findById(id))
                .thenReturn(Optional.empty());

        var thrown = Assertions.catchThrowable(() -> {
            bulletinService.update(id, newBulletin);
        });
        Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getItem_bulletin_notFound() {
        var id = 1L;
        Mockito.when(bulletinRepository.findById(id))
                .thenReturn(Optional.empty());

        var thrown = Assertions.catchThrowable(() -> {
            bulletinService.getItem(id);
        });
        Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

}