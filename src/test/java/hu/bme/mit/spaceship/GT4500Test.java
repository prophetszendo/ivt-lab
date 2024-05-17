package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primaryTorpedoStoreMock;
  private TorpedoStore secondaryTorpedoStoreMock;

  @BeforeEach
  public void init(){
    //this.ship = new GT4500();
    primaryTorpedoStoreMock = mock(TorpedoStore.class);
    secondaryTorpedoStoreMock = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStoreMock, secondaryTorpedoStoreMock);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    //assertEquals(true, result);
    assertTrue(result);
    verify(primaryTorpedoStoreMock, times(1)).fire(1);
    verify(secondaryTorpedoStoreMock, never()).fire(1);
   
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    //assertEquals(true, result);
    assertTrue(result);
    verify(primaryTorpedoStoreMock, times(1)).fire(1);
    verify(secondaryTorpedoStoreMock, times(1)).fire(1);
  }

  @Test
  public void singleFire_PrimaryNotEmpty_LastPrimary_Success(){
    //Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    assertTrue(result);
    verify(primaryTorpedoStoreMock, times(1)).fire(1);
    verify(secondaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void singleFire_SecondaryNotEmpty_LastSecondary_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    // Assert
    assertTrue(result);
    verify(secondaryTorpedoStoreMock, times(1)).fire(1);
    verify(primaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void singleFire_PrimaryEmpty_LastPrimary_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    // Assert
    assertTrue(result);
    verify(secondaryTorpedoStoreMock, times(1)).fire(1);
    verify(primaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void singleFire_BothEmpty(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    // Assert
    assertFalse(result);
    verify(primaryTorpedoStoreMock, never()).fire(1);
    verify(secondaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void allFire_BothNotEmpty_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    // Assert
    assertTrue(result);
    verify(primaryTorpedoStoreMock, times(1)).fire(1);
    verify(secondaryTorpedoStoreMock, times(1)).fire(1);
  }

  @Test
  public void allFire_SecondaryEmpty_PrimaryNonEmpty(){
    //Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    //Assert
    assertTrue(result);
    verify(primaryTorpedoStoreMock, times(1)).fire(1);
    verify(secondaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void singleFire_SecondaryNonEmpty_PrimaryEmpty() {
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true); // A másodlagos raktárból történő lövés sikerült

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStoreMock, never()).fire(1);
    verify(secondaryTorpedoStoreMock, times(1)).fire(1);
  }

  @Test
  public void singleFire_PrimaryNonEmpty_SecondaryFiredLast() {
    // Arrange
    when(!primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true); // Az elsődleges raktárból történő lövés sikerült
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    ship.fireTorpedo(FiringMode.SINGLE); // Az előző lövés a másodlagos raktárból történt

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
//    verify(secondaryTorpedoStoreMock, never()).fire(1);
  }

  @Test
  public void newTest(){
    when(!secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
  }

  /*@Test
    public void singleMode_WasPrimaryFiredLastTrue_SecondaryStoreNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        //assertTrue(result);
        //verify(secondaryTorpedoStoreMock, times(1)).fire(1);
        //verify(primaryTorpedoStoreMock, never()).fire(1);
    }*/

    @Test
    public void singleMode_WasPrimaryFiredLastTrue_SecondaryStoreEmpty_PrimaryStoreNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        /*assertTrue(result);
        verify(primaryTorpedoStoreMock, times(1)).fire(1);
        verify(secondaryTorpedoStoreMock, never()).fire(1);*/
    }

    @Test
    public void singleMode_WasPrimaryFiredLastTrue_BothStoresEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertFalse(result);
        verify(primaryTorpedoStoreMock, never()).fire(1);
        verify(secondaryTorpedoStoreMock, never()).fire(1);
    }

    @Test
    public void singleMode_WasPrimaryFiredLastFalse_PrimaryStoreNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result);
        verify(primaryTorpedoStoreMock, times(1)).fire(1);
        verify(secondaryTorpedoStoreMock, never()).fire(1);
    }

    @Test
    public void singleMode_WasPrimaryFiredLastFalse_SecondaryStoreNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result);
        verify(secondaryTorpedoStoreMock, times(1)).fire(1);
        verify(primaryTorpedoStoreMock, never()).fire(1);
    }

    @Test
    public void singleMode_WasPrimaryFiredLastFalse_BothStoresEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertFalse(result);
        verify(primaryTorpedoStoreMock, never()).fire(1);
        verify(secondaryTorpedoStoreMock, never()).fire(1);
    }

    @Test
    public void allMode_BothStoresNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
        when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert
        assertTrue(result);
        verify(primaryTorpedoStoreMock, times(1)).fire(1);
        verify(secondaryTorpedoStoreMock, times(1)).fire(1);
    }

    @Test
    public void allMode_PrimaryStoreEmpty_SecondaryStoreNotEmpty() {
        // Arrange
        when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
        when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);
        when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert
        assertTrue(result);
        verify(secondaryTorpedoStoreMock, times(1)).fire(1);
        verify(primaryTorpedoStoreMock, never()).fire(1);
    }
}
