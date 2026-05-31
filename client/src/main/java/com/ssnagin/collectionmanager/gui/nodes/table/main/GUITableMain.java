package com.ssnagin.collectionmanager.gui.nodes.table.main;


import com.ssnagin.collectionmanager.collection.model.MusicBand;
import com.ssnagin.collectionmanager.collection.model.MusicGenre;
import com.ssnagin.collectionmanager.gui.nodes.table.GUITable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GUITableMain extends GUITable<MusicBand> {

    private Integer currentLocalId = 1;
    protected TableColumn<Integer, Integer> localIdColumn;

    protected TableColumn<MusicBand, Long> idColumn;
    
    protected TableColumn<MusicBand, String> ownedUsernameColumn;
    
    protected TableColumn<MusicBand, String> nameColumn;
    
    protected TableColumn<MusicBand, Long> coordXColumn;
    
    protected TableColumn<MusicBand, Integer> coordYColumn;
    
    protected TableColumn<MusicBand, Long> participantsColumn;
    
    protected TableColumn<MusicBand, Integer> singlesColumn;
    
    protected TableColumn<MusicBand, String> bestAlbumNameColumn;
    
    protected TableColumn<MusicBand, Long> bestAlbumTracksColumn;
    
    protected TableColumn<MusicBand, String> genreColumn;

    public GUITableMain(TableView<MusicBand> tableView) {
        super(tableView);
    }

    @Override
    public void setProperties() {

//        localIdColumn.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(currentLocalId++));
//
//        tableView.getItems().addListener((ListChangeListener<MusicBand>) change -> {
//            currentLocalId = 1;
//        });

//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        coordXColumn.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getX()));
//
//        coordYColumn.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getY()));
//
//        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
//        singlesColumn.setCellValueFactory(new PropertyValueFactory<>("singlesCount"));
//
//        bestAlbumNameColumn.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getBestAlbum().getName()));
//        bestAlbumTracksColumn.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(cellData.getValue().getBestAlbum().getTracks()));
//
//        genreColumn.setCellValueFactory(cellData -> {
//            MusicGenre genre = cellData.getValue().getGenre();
//            return new SimpleStringProperty(genre != null ? genre.toString() : "");
//        });

        setEventFilters();
    }

    private void setEventFilters() {

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Копировать");
        copyItem.setOnAction(event -> copySelectedCellsToClipboard());

        // Добавляем сочетание клавиш для пункта меню
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        contextMenu.getItems().add(copyItem);
        tableView.setContextMenu(contextMenu);

//        tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.isControlDown() && event.getCode() == KeyCode.C) {
//                copySelectionToClipboard(tableView);
//                event.consume(); // Предотвращаем дальнейшую обработку события
//            }
//        });


    }

    // MOVE LATER DO NOT USE IT LIKE THAT IT IS BAD
    protected static <S> void copySelectionToClipboard(TableView<S> tableView) {

        try {
            StringBuilder clipboardString = new StringBuilder();

            // Получаем индексы выбранных строк
            ObservableList<Integer> selectedIndices = tableView.getSelectionModel().getSelectedIndices();

            // Если ничего не выбрано - выходим
            if (selectedIndices.isEmpty()) {
                return;
            }

            // Определяем границы выделения
            int minRowIndex = Integer.MAX_VALUE;
            int maxRowIndex = Integer.MIN_VALUE;
            for (Integer index : selectedIndices) {
                if (index < minRowIndex) {
                    minRowIndex = index;
                }
                if (index > maxRowIndex) {
                    maxRowIndex = index;
                }
            }

            // Копируем данные
            for (int row = minRowIndex; row <= maxRowIndex; row++) {
                for (int col = 0; col < tableView.getColumns().size(); col++) {
                    TableColumn<S, ?> column = tableView.getColumns().get(col);
                    Object cellData = column.getCellData(row);
                    clipboardString.append(cellData != null ? cellData.toString() : "");

                    // Добавляем разделитель между колонками (кроме последней)
                    if (col < tableView.getColumns().size() - 1) {
                        clipboardString.append('\t');
                    }
                }
                // Добавляем перенос строки (кроме последней строки)
                if (row < maxRowIndex) {
                    clipboardString.append('\n');
                }
            }

            // Устанавливаем содержимое в буфер обмена
            ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);
        } catch (Exception ignored) {

        }


    }


    private void copySelectedCellsToClipboard() {

        try {
            StringBuilder clipboardString = new StringBuilder();

            // Получаем выбранные ячейки
            ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();

            for (TablePosition position : selectedCells) {
                Object cellData = position.getTableColumn().getCellData(position.getRow());
                if (cellData != null) {
                    clipboardString.append(cellData.toString());
                }

                // Добавляем табуляцию между ячейками в строке
                if (position.getColumn() < tableView.getColumns().size() - 1) {
                    clipboardString.append('\t');
                }
            }

            // Добавляем перенос строки между строками
            if (selectedCells.size() > 1) {
                clipboardString.append('\n');
            }

            // Устанавливаем содержимое в буфер обмена
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString.toString());
            clipboard.setContent(content);
        } catch (Exception ignored) {}


    }
}
