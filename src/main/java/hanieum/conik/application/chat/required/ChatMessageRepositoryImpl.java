package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

//    @Override
//    public Map<Long, Long> findMaxSeqForRoomIds(Collection<Long> roomIds) {
//        if (roomIds == null || roomIds.isEmpty()) return Map.of();
//
//        // 1) pipeline
//        MatchOperation match = Aggregation.match(Criteria.where("roomId").in(roomIds));
//        GroupOperation group = Aggregation.group("roomId").max("seq").as("maxSeq");
//        Aggregation aggregation = Aggregation.newAggregation(match, group)
//                .withOptions(AggregationOptions.builder().allowDiskUse(true).build());
//
//        // 2) 안전한 컬렉션명
//        String collection = mongoTemplate.getCollectionName(ChatMessage.class);
//
//        AggregationResults<Document> results =
//                mongoTemplate.aggregate(aggregation, collection, Document.class);
//
//        // 3) 기본값 0으로 채워두고 결과 덮어쓰기
//        Map<Long, Long> map = new LinkedHashMap<>();
//        for (Long id : roomIds) map.put(id, 0L);
//
//        for (Document doc : results) {
//            Object idVal = doc.get("_id");   // NumberLong or Integer
//            Object maxVal = doc.get("maxSeq");
//            if (idVal instanceof Number && maxVal instanceof Number) {
//                map.put(((Number) idVal).longValue(), ((Number) maxVal).longValue());
//            }
//        }
//        return map;
//    }

    @Override
    public List<ChatMessageDto> findLastMessagesForRooms(Collection<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. roomId 필터링
        MatchOperation match = Aggregation.match(Criteria.where("roomId").in(roomIds));

        // 2. 각 roomId 내에서 seq 내림차순 정렬
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "seq");

        // 3. roomId로 그룹화하고, 각 그룹의 첫 번째 문서(가장 최신 메시지) 선택
        GroupOperation group = Aggregation.group("roomId")
                .first(Aggregation.ROOT).as("lastMessage");

        // 4. 집계 파이프라인 생성
        Aggregation aggregation = Aggregation.newAggregation(match, sort, group);

        // 5. 집계 실행
        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation,
                mongoTemplate.getCollectionName(ChatMessage.class),
                Document.class
        );

        // 6. 결과 DTO로 변환
        return results.getMappedResults().stream()
                .map(doc -> {
                    Document lastMessageDoc = doc.get("lastMessage", Document.class);
                    return ChatMessageDto.fromDocument(lastMessageDoc);
                })
                .collect(Collectors.toList());
    }
}
