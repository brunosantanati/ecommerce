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
        var producer = new KafkaProducer<String, String>(properties());
        var key = UUID.randomUUID().toString();
        var value = "132123,67523,7894589745";
        var record = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", key, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("sucesso enviando " + data.topic() + " ::: partition "
                    + data.partition() + " / offset " + data.offset() + " / timestamp " + data.timestamp());
        };
        var email = "Thank you for your order! We are processing your order!";
        var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);
        producer.send(record, callback).get();
        producer.send(emailRecord, callback).get();
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
