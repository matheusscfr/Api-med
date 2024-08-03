package ex.med.api.consulta;

import ex.med.api.domain.ConsultaDomain;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PlanilhaService {
    public ByteArrayInputStream gerarPlanilha(List<ConsultaDomain> consultas) throws IOException {
    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

        Sheet sheet = workbook.createSheet("Consultas");

        // Cabeçalhos da planilha
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Médico");
        headerRow.createCell(2).setCellValue("Paciente");
        headerRow.createCell(3).setCellValue("Data");
        headerRow.createCell(4).setCellValue("Motivo de Cancelamento");

        int rowNum = 1;
        for (ConsultaDomain consulta : consultas) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(consulta.getId());
            row.createCell(1).setCellValue(consulta.getMedico().getNome());
            row.createCell(2).setCellValue(consulta.getPaciente().getNome());
            row.createCell(3).setCellValue(consulta.getData().toString());
            row.createCell(4).setCellValue(consulta.getMotivoCacelamento().toString());
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
}
