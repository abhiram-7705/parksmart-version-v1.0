package com.cts.mfrp.parksmart.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "promo_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "promo_id"})
})
public class PromoUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "promo_id", nullable = false)
    private PromoCode promo;

    @Column(name = "used_at")
    @CreationTimestamp
    private LocalDateTime usedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    public PromoCode getPromo() { return promo; }
    public void setPromo(PromoCode promo) { this.promo = promo; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
}
