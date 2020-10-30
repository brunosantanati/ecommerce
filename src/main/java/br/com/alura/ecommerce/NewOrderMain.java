package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/*
ANTES DE EXECUTAR O METODO MAIN RODAR O KAFKA
C:\Users\55119>cd C:\kafka_2.13-2.6.0\bin\windows
C:\kafka_2.13-2.6.0\bin\windows>zookeeper-server-start.bat ..\..\config\zookeeper.properties
C:\kafka_2.13-2.6.0\bin\windows>kafka-server-start.bat ..\..\config\server.properties

PARA CONSUMIR AS MENSAGENS PRODUZIDAS POR ESSA CLASSE
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --from-beginning

CONFIGURAR 3 PARTICOES
kafka-topics.bat --alter --zookeeper localhost:2181 --topic ECOMMERCE_NEW_ORDER --partitions 3
 */

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var dispatcher = new KafkaDispatcher();

        for (var i=0; i < 10; i++) {
            var key = UUID.randomUUID().toString(); //A chave é usada para distribuir a mensagem entre as partições existentes e consequentemente entre as instâncias de um serviço dentro de um consumer group.
            var value = "132123,67523,7894589745";
            dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

            var email = "Thank you for your order! We are processing your order!";
            dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
        }
    }
}
