package com.rafaelresck.frete_viacep_wipro.consultacep;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("consulta-cep")
public class ConsultaCepAPI {
    @GetMapping("{cep}")
    public CepResultDTO consultaCep(@PathVariable("cep") String cep) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CepResultDTO> resp = restTemplate.getForEntity(
                String.format("https://viacep.com.br/ws/%s/json/", cep), CepResultDTO.class);
        CepResultDTO cepResultDTO = resp.getBody();
        if (cepResultDTO == null) {
            throw new RuntimeException("CEP não encontrado");
        }
        double frete = cepResultDTO.calcularFrete();
        cepResultDTO.setFrete(String.format("%.2f", frete));
        cepResultDTO.setLocalidade(cepResultDTO.getLocalidade()); // atualiza a propriedade "localidade" com o valor correto
        cepResultDTO.setLogradouro(cepResultDTO.getLogradouro()); // atualiza a propriedade "logradouro" com o valor correto
        return cepResultDTO;
    }
}

