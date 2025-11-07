package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Map<Long, Long> findMaxSeqForRoomIds(Collection<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) return Map.of();

        // 1) pipeline
        MatchOperation match = Aggregation.match(Criteria.where("roomId").in(roomIds));
        GroupOperation group = Aggregation.group("roomId").max("seq").as("maxSeq");
        Aggregation aggregation = Aggregation.newAggregation(match, group)
                .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        // 2) 안전한 컬렉션명
        String collection = mongoTemplate.getCollectionName(ChatMessage.class);

        AggregationResults<Document> results =
                mongoTemplate.aggregate(aggregation, collection, Document.class);

        // 3) 기본값 0으로 채워두고 결과 덮어쓰기
        Map<Long, Long> map = new LinkedHashMap<>();
        for (Long id : roomIds) map.put(id, 0L);

        for (Document doc : results) {
            Object idVal = doc.get("_id");   // NumberLong or Integer
            Object maxVal = doc.get("maxSeq");
            if (idVal instanceof Number && maxVal instanceof Number) {
                map.put(((Number) idVal).longValue(), ((Number) maxVal).longValue());
            }
        }
        return map;
    }
}
