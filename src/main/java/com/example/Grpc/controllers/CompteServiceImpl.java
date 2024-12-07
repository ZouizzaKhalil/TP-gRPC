package com.example.Grpc.controllers;

import io.grpc.stub.StreamObserver;
import ma.projet.grpc.stubs.Compte;
import ma.projet.grpc.stubs.CompteRequest;
import ma.projet.grpc.stubs.CompteServiceGrpc;
import ma.projet.grpc.stubs.GetAllComptesRequest;
import ma.projet.grpc.stubs.GetAllComptesResponse;
import ma.projet.grpc.stubs.GetCompteByIdRequest;
import ma.projet.grpc.stubs.GetCompteByIdResponse;
import ma.projet.grpc.stubs.GetTotalSoldeRequest;
import ma.projet.grpc.stubs.GetTotalSoldeResponse;
import ma.projet.grpc.stubs.SaveCompteRequest;
import ma.projet.grpc.stubs.SaveCompteResponse;
import ma.projet.grpc.stubs.SoldeStats;     // Make sure this matches the proto definition
import ma.projet.grpc.stubs.DeleteCompteRequest;
import ma.projet.grpc.stubs.DeleteCompteResponse;
import ma.projet.grpc.stubs.GetComptesByTypeRequest;
import ma.projet.grpc.stubs.GetComptesByTypeResponse;
import ma.projet.grpc.stubs.TypeCompte;

import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {

    private final Map<String, Compte> compteDB = new ConcurrentHashMap<>();

    @Override
    public void allComptes(GetAllComptesRequest request, StreamObserver<GetAllComptesResponse> responseObserver) {
        GetAllComptesResponse response = GetAllComptesResponse.newBuilder()
                .addAllComptes(compteDB.values())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request, StreamObserver<GetCompteByIdResponse> responseObserver) {
        Compte compte = compteDB.get(request.getId());
        if (compte != null) {
            responseObserver.onNext(
                    GetCompteByIdResponse.newBuilder().setCompte(compte).build()
            );
        } else {
            responseObserver.onError(new Throwable("Compte non trouvé"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request, StreamObserver<GetTotalSoldeResponse> responseObserver) {
        int count = compteDB.size();
        float sum = 0;
        for (Compte c : compteDB.values()) {
            sum += c.getSolde();
        }
        float average = (count > 0) ? (sum / count) : 0;

        SoldeStats stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        responseObserver.onNext(
                GetTotalSoldeResponse.newBuilder().setStats(stats).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request, StreamObserver<SaveCompteResponse> responseObserver) {
        CompteRequest compteReq = request.getCompte();
        String id = UUID.randomUUID().toString();

        Compte compte = Compte.newBuilder()
                .setId(id)
                .setSolde(compteReq.getSolde())
                .setDateCreation(compteReq.getDateCreation())
                .setType(compteReq.getType())
                .build();

        compteDB.put(id, compte);

        responseObserver.onNext(
                SaveCompteResponse.newBuilder().setCompte(compte).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCompte(DeleteCompteRequest request, StreamObserver<DeleteCompteResponse> responseObserver) {
        boolean removed = (compteDB.remove(request.getId()) != null);
        responseObserver.onNext(
                DeleteCompteResponse.newBuilder().setSuccess(removed).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getComptesByType(GetComptesByTypeRequest request, StreamObserver<GetComptesByTypeResponse> responseObserver) {
        TypeCompte requestedType = request.getType();
        var filtered = compteDB.values().stream()
                .filter(c -> c.getType().equals(requestedType))
                .collect(Collectors.toList());

        responseObserver.onNext(
                GetComptesByTypeResponse.newBuilder().addAllComptes(filtered).build()
        );
        responseObserver.onCompleted();
    }
}
