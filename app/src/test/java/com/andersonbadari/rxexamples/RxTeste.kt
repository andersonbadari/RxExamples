package com.andersonbadari.rxexamples

import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxTeste {

    @Test
    fun single_observable_single_observer_1() {

        // prepara para emitir 1 item (uma lista com 10 membros)
        val observable = Observable.just(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        // filtra a lista a ser emitida e cria uma lista nova com números pares apenas
        val pairObservable = observable.map { it.filter { it % 2 == 0 } }
        pairObservable.subscribe { it -> // emite o item
            // exibe 1 item emitido (lista com numeros pares)
            print("$it")
        }
    }

    @Test
    fun single_observable_single_observer_2() {

        // prepara para emitir 1 item (uma lista com 10 membros)
        val observable = Observable.just(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        // filtra a lista a ser emitida e cria uma lista nova com números pares apenas
        val pairObservable = observable.map { it.filter { it % 2 == 0 } }
        pairObservable.subscribe { it -> // emite o item
            // exibe 1 item emitido (lista com numeros pares)
            print("$it")
        }

        // filtra a lista a ser emitida e cria uma lista nova com números ímpares apenas
        val oddObservable = observable.map { it.filter { it % 2 != 0 } }
        oddObservable.subscribe { it -> // emite o item
            // exibe 1 item emitido (lista com numeros ímpares)
            print("$it")
        }
    }

    @Test
    fun single_observable_single_observer_3() {

        // prepara para emitir 10 itens (10 números inteiros)
        val observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        // filtra os itens a serem emitidos. apenas os números pares serao emitidos.
        val oddObservable = observable.filter { it % 2 == 0 }
        oddObservable.subscribe { it -> // emite os itens
            // emitirá 5 itens (os pares)
            print("$it, ")
        }
    }

    @Test
    fun single_observable_single_observer_4() {

        // observável é criado com o valor "fixo". no subscribe, o valor é emitido, mesmo que tenha mais de um subscribe
        val observable = Observable.just(System.currentTimeMillis())

        Thread.sleep(1000)
        val firstObservable = observable.map { it }
        firstObservable.subscribe { it ->
            print("$it\n")
        }

        Thread.sleep(1000)
        val secondObservable = observable.map { it }
        secondObservable.subscribe { it ->
            print("$it")
        }
    }

    @Test
    fun single_observable_single_observer_5() {

        // observável é criado com o valor "dinamico". no subscribe, o valor é emitido, mesmo que tenha mais de um subscribe
        val observable = Observable.defer { Observable.just(System.currentTimeMillis()) }

        Thread.sleep(1000)
        val firstObservable = observable.map { it }
        firstObservable.subscribe { it ->
            print("$it\n")
        }

        Thread.sleep(1000)
        val secondObservable = observable.map { it }
        secondObservable.subscribe { it ->
            print("$it")
        }
    }

    @Test
    fun single_observable_multi_observers() {

        // prepara para emitir 1 item (uma lista com 10 membros)
        val connectableObservable = ConnectableObservable.just(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).publish()

        Thread.sleep(1000)
        connectableObservable.subscribe {
            // prepara para emitir a lista bruta
            print("Raw List: $it\n")
        }

        Thread.sleep(1000)
        connectableObservable.map { it.filter { it % 2 == 0 } }.subscribe {
            // prepara para emitir numeros pares
            print("Even List: $it\n")
        }

        Thread.sleep(1000)
        connectableObservable.map { it.filter { it % 2 != 0 } }.subscribe {
            // prepara para emitir números ímpares
            print("Odd List: $it\n")
        }


        connectableObservable.subscribe {
            // prepara para emitir a bruta
            print("All values emmited when connect() is called")
        }

        Thread.sleep(1000)

        // emite os itens. todos subscribers serao chamados sequencialmente
        connectableObservable.connect()
    }

    @Test
    // emite para todos os subscribers
    fun publish_subject_1() {

        // prepara o fluxo de dados de números inteiros
        val integerObservable = PublishSubject.create<Int>()

        // filtra o fluxo de dados de números inteiros e só printa os pares
        integerObservable.filter { it % 2 == 0 }.subscribe {
            print("Par: $it, ")
        }

        // filtra o fluxo de dados de números inteiros e só printa os ímpares
        integerObservable.filter { it % 2 != 0 }.subscribe {
            print("Ímpar: $it, ")
        }

        // emite os itens
        integerObservable.onNext(1)
        integerObservable.onNext(2)
        integerObservable.onNext(3)
        integerObservable.onNext(4)

    }

    @Test
    // emite para todos os subscribers
    fun publish_subject_2() {

        // prepara o fluxo de dados de números inteiros
        val integerObservable = PublishSubject.create<Int>()

        // exibe todos os itens
        integerObservable.subscribe {
            print("Primeiro sub: $it, ")
        }

        // exibe todos os itens
        integerObservable.subscribe {
            print("Segundo sub: $it, ")
        }

        // emite os itens
        integerObservable.onNext(1)
        integerObservable.onNext(2)
        integerObservable.onNext(3)
        integerObservable.onNext(4)

    }

    @Test
    fun replay_subject() {

        // prepara o fluxo de dados de números inteiros
        val integerObservable = ReplaySubject.create<Int>()

        // exibe todos os itens
        integerObservable.subscribe {
            print("Primeiro sub: $it, ")
        }

        // emite os itens
        integerObservable.onNext(1)
        integerObservable.onNext(2)
        integerObservable.onNext(3)
        integerObservable.onNext(4)
        integerObservable.onComplete()
        print("\n")
        integerObservable.onNext(5) // nao será emitido

        // exibe todos os itens novamente
        integerObservable.subscribe {
            print("Segundo sub: $it, ")
        }

        integerObservable.onNext(5) // nao será emitido
    }

    @Test
    fun behaviour_subject() {

        // prepara o fluxo de dados de números inteiros
        val integerObservable = BehaviorSubject.create<Int>()

        // exibe todos os itens
        integerObservable.subscribe {
            print("Primeiro sub: $it, ")
        }

        // emite os itens
        integerObservable.onNext(1)
        integerObservable.onNext(2)
        integerObservable.onNext(3)
        integerObservable.onNext(4)
        print("\n")

        // exibe o ultimo item emitido (4)
        integerObservable.subscribe {
            print("Segundo sub: $it, ")
        }

        integerObservable.onNext(5) // exibe no primeiro e no segundo subscriber
    }

    @Test
    fun async_subject() {

        // prepara o fluxo de dados de números inteiros
        val integerObservable = AsyncSubject.create<Int>()

        // exibirá só o ultimo item apenas após o onComplete() ser chamado
        integerObservable.subscribe {
            print("Primeiro sub: $it, ")
        }

        // emite os itens
        integerObservable.onNext(1)
        integerObservable.onNext(2)
        integerObservable.onNext(3)
        integerObservable.onNext(4)

        // exibirá só o ultimo item apenas após o onComplete() ser chamado
        integerObservable.subscribe {
            print("Primeiro sub: $it, ")
        }

        integerObservable.onComplete() // inicia a emissao do ultimo item

    }

    @Test
    fun flat_map_example() {

        val firstObservable = Observable.just(1)
        val secondObservable = Observable.just(2)

        // para cada item emitido pelo primeiro observável (firstObservable), emite outro observável (secondObservable)
        firstObservable.flatMap { secondObservable }.subscribe {
            print(it)
        }

    }

















    @Test
    fun example_in_android_application_1() {
        // success case

        class Interactor_1 {
            fun getUsername(): Observable<String>{
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    emitter.onNext("jose silva")
                    emitter.onComplete()
                }
            }
        }
        class ViewModel_1 {
            private val interator = Interactor_1()
            fun getUsername(): Observable<String> {
                return interator.getUsername().onErrorReturn { "exception thrown" }
            }
        }

        // in activity, fragment, etc
        val vm = ViewModel_1()
        vm.getUsername().subscribe {
            val username = it ?: ""
            print("subscribed: $username") // myTextView.text = username
        }

    }

    @Test
    fun example_in_android_application_2() {
        // error case

        class Interactor_2 {
            fun getUsername(): Observable<String>{
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    val error = 1/0 // will throw an exception
                    emitter.onNext("jose silva")
                    emitter.onComplete()
                }
            }
        }
        class ViewModel_2 {
            private val interator = Interactor_2()
            fun getUsername(): Observable<String> {
                return interator.getUsername()
                    .onErrorReturn { it.localizedMessage }
            }
        }

        // in activity, fragment, etc
        val vm = ViewModel_2()
        vm.getUsername().subscribe {
            val username = it ?: ""
            print("subscribed: $username") // myTextView.text = username
        }

    }

    /**
     * Exemplo com publish subject de um aplicativo para depósito bancário
     */
    @Test
    fun example_in_android_application_3() {
        // bank deposit application

        val add_value_button_click = PublishSubject.create<Int>()
        val add_observable = add_value_button_click.map { 500 }
        val remove_value_button_click = PublishSubject.create<Int>()
        val remove_observable = remove_value_button_click.map { -500 }

        add_observable.mergeWith(remove_observable)
            .startWith(0)
            .scan { t1: Int, t2: Int -> t1 + t2 }
            .map { it.toString() }
            .subscribe {
                print("$it\n")
            }

        // apesar de emitir 4 itens intencionalmente, o starts with emite o valor inicial...total de itens emitidos = 5
        add_value_button_click.onNext(0) // clicked in plus R$ 500 | total R$ 500
        add_value_button_click.onNext(0) // clicked in plus R$ 500 | total R$ 1000
        add_value_button_click.onNext(0) // clicked in plus R$ 500 | total R$ 1500
        remove_value_button_click.onNext(0) // clicked in minus R$ 500 | total R$ 1000

    }

    @Test
    fun example_in_android_application_4() {
        // Exemplo de duas requisicoes em paralelo esperando uma única chegada

    }

    @Test
    fun example_in_android_application_5() {
        // Exemplo de duas requisicoes em que a segunda tem como entrada um parametro da primeira

    }


    /**
     * Exemplo com duas requisicoes dependentes uma da outra
     */
    @Test
    fun example_in_android_application_6_flat_map() {

        class Repository {
            fun getUserIdByCPF(cpf: String): Observable<String>{
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    println("getUserIdByCPF")
                    emitter.onNext(cpf.reversed()) // return user id
                    emitter.onComplete()
                }
            }
            fun getUserNameById(userId: String): Observable<String> {
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    println("getUserNameById")
                    emitter.onNext("Jose silva") // return user name
                    emitter.onComplete()
                }
            }
        }

        class GetUserUseCase() {
            val repository = Repository()

            fun getUser(cpf: String): Observable<String> {
                return getUserIdByCPFObservable(cpf).flatMap { getUserNameByIdObservable(it) }
            }

            private fun getUserIdByCPFObservable(cpf: String): Observable<String> {
                return repository.getUserIdByCPF(cpf)
            }

            private fun getUserNameByIdObservable(id: String): Observable<String> {
                return repository.getUserNameById(id)
            }
        }

        class ViewModel {
            private val userUseCase = GetUserUseCase()
            fun getUser(cpf: String): Observable<String> {
                return userUseCase.getUser(cpf).onErrorReturn { it.localizedMessage }
            }
        }

        // in activity, fragment, etc
        val vm = ViewModel()
        vm.getUser("012.345.678.90").subscribe {
            val username = it ?: ""
            print("subscribed: $username") // myTextView.text = username
        }

    }


    @Test
    fun example_in_android_application_7_multiple_clicks() {

        Observable.just(1, 2, 3, 4) // usuário clicando 4 vezes muito rapidamente
            // em um intervalo de 300 milissegundos, emite apenas o primeiro item
            // em outras palavras, o usuário somente poderá clicar 1 vez no máximo dentro do intervalo de 300ms
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .subscribe {
                print("Clickou a $it ª vez apenas detro do intervalo de 300 ms")
            }

    }


    /**
     * Exemplo com duas requisicoes independentes uma da outra
     */
    @Test
    fun example_in_android_application_8_flat_map() {

        class Repository {
            fun getUserIdByCPF(): Observable<String>{
                return Observable.create { emitter ->
                    Thread.sleep(1500) // delay in network
                    println("getUserIdByCPF: "+Thread.currentThread().toString().plus("\n"))
                    emitter.onNext("josé ") // return user name
                    emitter.onComplete()
                }
            }
            fun getUserNameById(): Observable<String> {
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    println("getUserNameById: "+Thread.currentThread().toString().plus("\n"))
                    emitter.onNext("silva") // return user surname
                    emitter.onComplete()
                }
            }
        }

        class GetUserUseCase() {
            val repository = Repository()

            fun getUser(): Observable<String> {
                return getUserNameObservable().mergeWith(getUserSurnameObservable())
                // espera ambos observáveis serem emitidos na ordem especificada
            }

            private fun getUserNameObservable(): Observable<String> {
                return repository.getUserIdByCPF()
            }

            private fun getUserSurnameObservable(): Observable<String> {
                return repository.getUserNameById()
            }
        }

        class ViewModel {
            private val userUseCase = GetUserUseCase()
            fun getUser(): Observable<String> {
                return userUseCase.getUser().onErrorReturn { it.localizedMessage }
            }
        }

        // in activity, fragment, etc
        val vm = ViewModel()
        vm.getUser()
//            .subscribeOn(Schedulers.newThread())
//            .blockingSubscribe() {
            .subscribe() {
                val username = it ?: ""
                print("blockingSubscribe: $username\n") // myTextView.text = username
                println("blockingSubscribe: "+Thread.currentThread().toString().plus("\n"))
            }

    }

    @Test
    fun multi_threading_1() {

        Observable.just(1, 2) // emite 4 itens na thread principal
            .map {
                print("Valor $it sendo emitido na thread principal: ".plus(Thread.currentThread().toString()).plus("\n"))
                it
            }
            .observeOn(Schedulers.newThread()) // observáveis (quem consome os dados) estarão nesta nova thread
            .subscribe {
                print("Valor $it sendo emitido na thread paralela: ".plus(Thread.currentThread().toString()).plus("\n"))
            }

    }

    @Test
    fun multi_threading_2() {

        Observable.just(1, 2) // emite 2 itens na thread...
            .map {
                print("Valor $it sendo emitido na thread paralela: ".plus(Thread.currentThread().toString()).plus("\n"))
                it
            }
            .subscribeOn(Schedulers.newThread()) // observáveis (fonte de dados) emitirão itens nesta nova thread
            .blockingSubscribe { // observadores consomem os itens emitidos na thread principal
                print("Valor $it sendo emitido na thread principal: ".plus(Thread.currentThread().toString()).plus("\n"))
            }

    }

    @Test
    fun multi_threading_3() {

        Observable.just(1, 2) // emite 2 itens na thread...
            .subscribeOn(Schedulers.newThread()) // // observáveis (fonte de dados) emitirão itens nesta nova thread
            .map {
                print("Valor $it sendo emitido na thread paralela: ".plus(Thread.currentThread().toString()).plus("\n"))
                it
            }
            .blockingSubscribe { // observadores consomem os itens emitidos na thread principal
                print("Valor $it sendo emitido na thread principal: ".plus(Thread.currentThread().toString()).plus("\n"))
            }

    }

    /**
     * Exemplo com duas requisicoes independentes uma da outra (feitas em outra thread)
     */
    @Test
    fun example_in_android_application_8_multi_threading() {

        class Repository {
            fun getUserIdByCPF(): Observable<String>{
                return Observable.create { emitter ->
                    Thread.sleep(1500) // delay in network
                    println("getUserIdByCPF: "+Thread.currentThread().toString().plus("\n"))
                    emitter.onNext("josé ") // return user name
                    emitter.onComplete()
                }
            }
            fun getUserNameById(): Observable<String> {
                return Observable.create { emitter ->
                    Thread.sleep(1000) // delay in network
                    println("getUserNameById: "+Thread.currentThread().toString().plus("\n"))
                    emitter.onNext("silva") // return user surname
                    emitter.onComplete()
                }
            }
        }

        class GetUserUseCase() {
            val repository = Repository()

            fun getUser(): Observable<String> {
                return getUserNameObservable().mergeWith(getUserSurnameObservable())
                // espera ambos observáveis serem emitidos na ordem especificada
            }

            private fun getUserNameObservable(): Observable<String> {
                return repository.getUserIdByCPF()
            }

            private fun getUserSurnameObservable(): Observable<String> {
                return repository.getUserNameById()
            }
        }

        class ViewModel {
            private val userUseCase = GetUserUseCase()
            fun getUser(): Observable<String> {
                return userUseCase.getUser().onErrorReturn { it.localizedMessage }
            }
        }

        // in activity, fragment, etc
        val vm = ViewModel()
        vm.getUser()
            .subscribeOn(Schedulers.newThread())
            .blockingSubscribe {
                val username = it ?: ""
                print("blockingSubscribe: $username\n") // myTextView.text = username
                println("blockingSubscribe: "+Thread.currentThread().toString().plus("\n"))
            }

    }

    /**
     *
     *
     * That’s it for hot and cold observables. Remember, data-driven is cold, event-driven is hot.
     *
     */

    @Test
    fun cold_observable() {

        // emite itens na subscricao (interessante para buscar dados de alguma fonte)

        val originalObservable = Observable.just(1, 2, 3)

        originalObservable.subscribe {
            println("primeiro subscribe: $it")
        }

        originalObservable.subscribe {
            println("segundo subscribe: $it")
        }

    }

    @Test
    fun hot_observable () {

        // emite itens quando algum evento ocorre, como um clique de botao (interessante para eventos de UI)

        val originalObservable = Observable.just(1, 2, 3).publish()

        originalObservable.subscribe {
            println("primeiro subscribe: $it")
        }


        originalObservable.subscribe {
            println("segundo subscribe: $it")
        }

        originalObservable.connect()

    }

}
