package com.bk.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ExpectedException;

import com.bk.hotel.RoomServiceException;
import com.bk.hotel.model.Room;
import com.bk.hotel.repo.RoomRepo;

@EnableRuleMigrationSupport
public class RoomServiceImplTest {
	private List<String> roomTypes = Arrays.asList("Single", "Double", "Suite");
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testFindByValidRoomType() {
		RoomRepo repo = mock(RoomRepo.class);
		RoomServiceImpl service = new RoomServiceImpl(repo, roomTypes);
		when(repo.findRoomsByRoomType(any(String.class))).thenReturn(Arrays.asList(//
				new Room(1L, "100", "Single", new BigDecimal(145.99))));
		List<Room> rooms = service.findRoomsByType("Single");

		assertEquals(1, rooms.size());
	}

	@Test
	public void testFindByInvalidRoomType() {
		RoomRepo repo = mock(RoomRepo.class);
		RoomServiceImpl service = new RoomServiceImpl(repo, roomTypes);
		verify(repo, times(0)).findRoomsByRoomType(any());
		expectedException.expect(RoomServiceException.class);
		expectedException.expectMessage("Room type: NOT FOUND not found!");
		service.findRoomsByType("NOT FOUND");
	}

	@Test
	public void testFindByNullRoomType() {
		RoomRepo repo = mock(RoomRepo.class);
		RoomServiceImpl service = new RoomServiceImpl(repo, roomTypes);
		verify(repo, times(0)).findRoomsByRoomType(any());
		RoomServiceException e = assertThrows(RoomServiceException.class, () -> service.findRoomsByType(null));
		assertEquals("Room type: null not found!", e.getMessage());
	}

	@Test
	@DisplayName("SOme cool name")
	public void testAddRoom() {
		RoomRepo repo = mock(RoomRepo.class);
		when(repo.save(any())).thenReturn(new Room(1L, "100", "Single", new BigDecimal(149.99)));
		RoomServiceImpl service = new RoomServiceImpl(repo, roomTypes);

		Room newRoom = service.addRoom(new Room());
		
		assertAll(
		() -> assertThat(newRoom.getId()).isEqualTo(2L),
		() -> assertEquals("200", newRoom.getRoomNumber()),
		() -> assertEquals("Dingle", newRoom.getRoomType()),
		() -> assertEquals(new BigDecimal(249.99), newRoom.getRoomRate())
		);
	}

}
