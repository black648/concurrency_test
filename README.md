재고처리 동시성 이슈에 대한 테스트

* 개발환경
  * JDK : 11
  * SpringBoot : 2.7.15
  * DB : MariaDB
  * ETC : Docker

* Synchronized
  * 하나의 프로세스 안에서만 보장되어 서버가 다중일 경우는 문제가 생길 수 있음.

* RDB
  * Pessimistic Lock 이용
    * 성능감소가 있을 수 있음.
    * 충돌이 빈번할 것 같으면 사용하면 좋음
  * Optimistic Lock 이용 (version)
    * 별도의 락을 잡지 않으므로 성능적 이점이 있음
    * 실패 시 재시도 로직을 개발자가 직접 구현해야 함.
  * Named Lock 이용
    * 주로 분산 Lock을 할때 사용
    * 타임아웃 설정하기 편함
    * 데이터 삽입 시 정합성 맞추기 좋음
    * 트랜잭션 종료 시 락 해제, 세션관리를 개발자가 잘 해주어야 하기 때문에 주의가 필요.. (구현이 복잡할 수 있음)

* Redis
  * Lettuce
    * 구현이 간단
    * spring data redis를 이용하면 lettuce가 기본이기 때문에 별도 라이브러리를 사용하지 않아도 됨. (redisTemplate.opsForValue().setIfAbsent)
    * spin lock 방식이기 때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis에 부하가 생길 수 있음
  * Redisson
    * 락 획득 재시도를 기본으로 제공
    * publish, subscribe 방식으로 구현되어 있기 때문에 lettuce와 비교했을 때 redis 부하가 덜하다.
    * lock을 라이브러리 차원에서 제공해주기 때문에 사용법 학습이 필요.

  * 실무
    * 재시도가 필요하지 않은 lock 은 lettuce
    * 재시도가 필요한 경우는 redisson

* 결론
  * DB (Mysql)
    * 이미 RDB를 사용하고 있다면 별도 비용없이 사용가능
    * 어느정도의 트래픽까지는 문제없이 활용 가능
    * Redis보다 성능이 좋지 않음
  * Redis
    * 별도의 구축비용이 필요
    * MySQL보다 성능이 좋지 않 다.