// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CompteService.proto

package ma.projet.grpc.stubs;

public interface GetComptesByTypeResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:GetComptesByTypeResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * List of comptes of the specified type
   * </pre>
   *
   * <code>repeated .Compte comptes = 1;</code>
   */
  java.util.List<ma.projet.grpc.stubs.Compte> 
      getComptesList();
  /**
   * <pre>
   * List of comptes of the specified type
   * </pre>
   *
   * <code>repeated .Compte comptes = 1;</code>
   */
  ma.projet.grpc.stubs.Compte getComptes(int index);
  /**
   * <pre>
   * List of comptes of the specified type
   * </pre>
   *
   * <code>repeated .Compte comptes = 1;</code>
   */
  int getComptesCount();
  /**
   * <pre>
   * List of comptes of the specified type
   * </pre>
   *
   * <code>repeated .Compte comptes = 1;</code>
   */
  java.util.List<? extends ma.projet.grpc.stubs.CompteOrBuilder> 
      getComptesOrBuilderList();
  /**
   * <pre>
   * List of comptes of the specified type
   * </pre>
   *
   * <code>repeated .Compte comptes = 1;</code>
   */
  ma.projet.grpc.stubs.CompteOrBuilder getComptesOrBuilder(
      int index);
}
