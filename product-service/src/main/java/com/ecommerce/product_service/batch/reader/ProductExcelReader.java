package com.ecommerce.product_service.batch.reader;

import lombok.SneakyThrows;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ecommerce.product_service.dto.ProductExcelRowDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@Component
@StepScope
public class ProductExcelReader implements ItemReader<ProductExcelRowDto> {

    private final Workbook workbook;
    private final Iterator<Row> rowIterator;
    private final DataFormatter dataFormatter = new DataFormatter();

    @SneakyThrows
    public ProductExcelReader(@Value("#{jobParameters['filePath']}") String filePath) {
        FileInputStream inputStream = new FileInputStream(filePath);

        this.workbook = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(inputStream);

        Sheet sheet = this.workbook.getSheetAt(0);

        this.rowIterator = sheet.iterator();

        if (this.rowIterator.hasNext()) {
            this.rowIterator.next(); // skip header row
        }
    }

    @Override
    public ProductExcelRowDto read() throws Exception {
        if (!this.rowIterator.hasNext()) {
            closeWorkbook();
            return null;
        }

        Row row = this.rowIterator.next();

        ProductExcelRowDto excelRow = new ProductExcelRowDto();

        excelRow.setSku(setString(row.getCell(1)));
        excelRow.setSlug(setString(row.getCell(2)));

        return excelRow;
    }

    private void closeWorkbook() {
        try {
            this.workbook.close();
        } catch (IOException e) {
            // log or ignore — stream already exhausted
        }
    }

    private String setString(Cell cell) {
        if (cell == null) {
            return null;
        }

        return dataFormatter.formatCellValue(cell);
    }

}
